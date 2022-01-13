package com.internship.internship.repository;

import com.internship.internship.exeption.DataNotFoundException;
import com.internship.internship.mapper.GroupMapper;
import com.internship.internship.mapper.PriorityMapper;
import com.internship.internship.mapper.TaskMapper;
import com.internship.internship.model.Composite.CompositeTask;
import com.internship.internship.model.Group;
import com.internship.internship.model.Priority;
import com.internship.internship.model.Task;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Transactional
@Repository
public class GroupRepo {

    private static final Logger LOGGER = LoggerFactory.getLogger(GroupRepo.class);
    private final JdbcTemplate jdbcTemplate;
    private final NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    public GroupRepo(JdbcTemplate jdbcTemplate, NamedParameterJdbcTemplate namedParameterJdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
        this.namedParameterJdbcTemplate = namedParameterJdbcTemplate;
    }

    public Group getGroupById(Long id) {
        String sql = "select * from group_of_tasks g left join person p on p.id = g.id_person where g.id = ?";
        try {
            Group group = jdbcTemplate.queryForObject(sql, new GroupMapper(), id);
            group.setTasks(getCompositeTasks(id));
            if (group.isPriority()) {
                group.setPriorityList(getAllPriorityByGroupId(group.getId()));
            }
            return group;
        } catch (EmptyResultDataAccessException exception) {
            LOGGER.warn("handling 404 error on getGroupById method");

            throw new DataNotFoundException(String.format("Group Id %d is not found", id));
        }
    }

    public List<Group> getAll() {
        String sql = "select * from group_of_tasks g left join person p on p.id = g.id_person";
        List<Group> groups = jdbcTemplate.query(sql, new GroupMapper());
        for (Group group : groups) {
            group.setTasks(getCompositeTasks(group.getId()));
            if (group.isPriority()) {
                group.setPriorityList(getAllPriorityByGroupId(group.getId()));
            }
        }
        return groups;
    }

    public Integer addGroup(MapSqlParameterSource parameters) {
        String sql = "insert into group_of_tasks ( id, name) values (:id, :name)";

        return namedParameterJdbcTemplate.update(sql, parameters);
    }

    public Integer updateGroup(Group group) {
        String sql = "update group_of_tasks set name = ? where id = ?;";

        return jdbcTemplate.update(sql, group.getName(), group.getId());
    }

    public Integer deleteGroup(Long id) {
//        String deleteTasksSql = "delete from task where id in (select id_task from task_group where id_group = ?);";
//        String deleteGroupsInSql = "delete from group_of_tasks where id in (select id_child from group_in_group where id_parent = ?);";
        String deleteGroupSql = "delete from group_of_tasks where id = ?;";

//        jdbcTemplate.update(deleteTasksSql, id);
//        jdbcTemplate.update(deleteGroupsInSql, id);

        return jdbcTemplate.update(deleteGroupSql, id);
    }

    public Integer addTaskToGroup(Long id, Long taskId) {
        String sql = "update task set id_group = ? where id = ?";
        return jdbcTemplate.update(sql, id, taskId);
    }

    public Integer deleteTaskFromGroup(Long idGroup, Long idTask) {
        String sql = "update task set id_group = null where id = ? and id_group = ?";

        return jdbcTemplate.update(sql, idTask, idGroup);
    }

    public List<Task> getTasksById(Long id) {
        String sqlForGroup = "select * from task where id_group = ?";

        return jdbcTemplate.query(sqlForGroup, new TaskMapper(), id);
    }

    public Integer setPriority(Long id, boolean flag) {
        String sql = "update group_of_tasks set priority = ? where id = ?";
        return jdbcTemplate.update(sql, flag, id);
    }

    public List<Priority> getAllPriorityByGroupId(Long id) {
        String sql = "select * from priority_of_task p where p.id_group = ?";

        return jdbcTemplate.query(sql, new PriorityMapper(), id);
    }

    private CompositeTask getCompositeTasks(Long id) {
        CompositeTask compositeTasks = new CompositeTask();

        List<Task> taskList = getTasksById(id);
        compositeTasks.addAll(taskList);

        List<Group> groupList = getAllGroupInGroup(id);
        if (groupList.size() > 0) {
            for (Group group : groupList) {
                group.setTasks(getCompositeTasks(group.getId()));
            }
        }
        compositeTasks.addAll(groupList);

        return compositeTasks;
    }

    private List<Group> getAllGroupInGroup(Long id) {
        String sql = "select * from group_of_tasks where id_parent = ?;";
        List<Group> groups = jdbcTemplate.query(sql, new GroupMapper(), id);
        return groups;
    }

    public Integer addGroupToGroup(Long id, Long idGroup) {
        String sql = "update group_of_tasks set id_parent = ? where id = ?;";

        return jdbcTemplate.update(sql, id, idGroup);
    }

    public Integer deleteGroupFromGroup(Long id, Long idGroup) {
        String sql = "update group_of_tasks set id_parent = null where id_parent = ? and id = ?";

        return jdbcTemplate.update(sql, id, idGroup);
    }
}
