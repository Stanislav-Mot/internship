package com.internship.internship.repository;

import com.internship.internship.exeption.DataNotFoundException;
import com.internship.internship.mapper.GroupMapper;
import com.internship.internship.mapper.TaskMapper;
import com.internship.internship.model.Assignment;
import com.internship.internship.model.Group;
import com.internship.internship.model.Task;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Transactional
@Repository
public class GroupRepo {

    private static final Logger LOGGER = LoggerFactory.getLogger(GroupRepo.class);
    private final JdbcTemplate jdbcTemplate;
    private final NamedParameterJdbcTemplate namedParameterJdbcTemplate;
    private final SimpleJdbcInsert simpleJdbcInsert;

    public GroupRepo(JdbcTemplate jdbcTemplate, NamedParameterJdbcTemplate namedParameterJdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
        this.namedParameterJdbcTemplate = namedParameterJdbcTemplate;
        this.simpleJdbcInsert = new SimpleJdbcInsert(jdbcTemplate);
    }

    public Group getGroupById(Long id) {
        String sql = "select * from group_of_tasks where id = ?";
        try {
            Group group = jdbcTemplate.queryForObject(sql, new GroupMapper(), id);
            group.setTasks(getComposite(id));

            return group;
        } catch (EmptyResultDataAccessException exception) {
            LOGGER.warn("handling 404 error on getGroupById method");

            throw new DataNotFoundException(String.format("Group Id %d is not found", id));
        }
    }

    public List<Group> getByPersonId(Long id) {
        String sql = "select * from  group_of_tasks got left join person_group pg on got.id = pg.id_group where pg.id_person = ?";
        List<Group> groups = jdbcTemplate.query(sql, new GroupMapper(), id);
        for (Group group : groups) {
            group.setTasks(getComposite(group.getId()));
        }
        return groups;
    }

    public List<Group> getAll() {
        String sql = "select * from group_of_tasks";
        List<Group> groups = jdbcTemplate.query(sql, new GroupMapper());
        for (Group group : groups) {
            group.setTasks(getComposite(group.getId()));
        }
        return groups;
    }

    public void addGroup(MapSqlParameterSource parameters, KeyHolder holder) {
        String sql = "insert into group_of_tasks (name) values (:name)";

        namedParameterJdbcTemplate.update(sql, parameters, holder);
    }

    public Integer updateGroup(Group group) {
        String sql = "update group_of_tasks set name = ? where id = ?;";

        return jdbcTemplate.update(sql, group.getName(), group.getId());
    }

    public Integer deleteGroup(Long id) {
        String deleteGroupSql = "delete from group_of_tasks where id = ?;";

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

    private List<Assignment> getComposite(Long id) {
        List<Assignment> assignments = new ArrayList<>();

        List<Task> taskList = getTasksById(id);
        assignments.addAll(taskList);

        List<Group> groupList = getAllGroupInGroup(id);
        if (groupList.size() > 0) {
            for (Group group : groupList) {
                group.setTasks(getComposite(group.getId()));
            }
        }
        assignments.addAll(groupList);

        return assignments;
    }

    private List<Group> getAllGroupInGroup(Long id) {
        String sql = "select * from group_of_tasks where id_parent = ?;";

        return jdbcTemplate.query(sql, new GroupMapper(), id);
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
