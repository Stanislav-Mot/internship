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
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Transactional
@Repository
public class GroupRepo {

    private final Logger LOGGER = LoggerFactory.getLogger(GroupRepo.class);
    private final JdbcTemplate jdbcTemplate;
    private final NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    public GroupRepo(JdbcTemplate jdbcTemplate, NamedParameterJdbcTemplate namedParameterJdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
        this.namedParameterJdbcTemplate = namedParameterJdbcTemplate;
    }

    public Group getGroupById(Long id) {
        String sql = "SELECT * FROM group_of_tasks WHERE id = ?";
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
        String sql = "SELECT * FROM  group_of_tasks got LEFT JOIN person_group pg ON got.id = pg.id_group WHERE pg.id_person = ?";
        List<Group> groups = jdbcTemplate.query(sql, new GroupMapper(), id);
        for (Group group : groups) {
            group.setTasks(getComposite(group.getId()));
        }
        return groups;
    }

    public List<Group> getAll() {
        String sql = "SELECT * FROM group_of_tasks";
        List<Group> groups = jdbcTemplate.query(sql, new GroupMapper());
        for (Group group : groups) {
            group.setTasks(getComposite(group.getId()));
        }
        return groups;
    }

    public void addGroup(MapSqlParameterSource parameters, KeyHolder holder) {
        String sql = "INSERT INTO group_of_tasks (name) VALUES (:name)";

        namedParameterJdbcTemplate.update(sql, parameters, holder);
    }

    public Group updateGroup(Group group) {
        String sql = "UPDATE group_of_tasks SET name = ? WHERE id = ?;";
        jdbcTemplate.update(sql, group.getName(), group.getId());

        return getGroupById(group.getId());
    }

    public Integer deleteGroup(Long id) {
        String deleteGroupSql = "DELETE FROM group_of_tasks WHERE id = ?;";

        return jdbcTemplate.update(deleteGroupSql, id);
    }

    public Group addTaskToGroup(Long id, Long taskId) {
        String sql = "UPDATE task SET id_group = ? WHERE id = ?";
        jdbcTemplate.update(sql, id, taskId);
        return getGroupById(id);
    }

    public Integer deleteTaskFromGroup(Long idGroup, Long idTask) {
        String sql = "UPDATE task SET id_group = NULL WHERE id = ? AND id_group = ?";

        return jdbcTemplate.update(sql, idTask, idGroup);
    }

    public List<Task> getTasksById(Long id) {
        String sqlForGroup = "SELECT * FROM task WHERE id_group = ?";

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
        String sql = "SELECT * FROM group_of_tasks WHERE id_parent = ?;";

        return jdbcTemplate.query(sql, new GroupMapper(), id);
    }

    public Group addGroupToGroup(Long id, Long idGroup) {
        String sql = "UPDATE group_of_tasks SET id_parent = ? WHERE id = ?;";
        jdbcTemplate.update(sql, id, idGroup);
        return getGroupById(id);
    }

    public Integer deleteGroupFromGroup(Long id, Long idGroup) {
        String sql = "UPDATE group_of_tasks SET id_parent = NULL WHERE id_parent = ? AND id = ?";

        return jdbcTemplate.update(sql, id, idGroup);
    }
}
