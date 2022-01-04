package com.internship.internship.repository;

import com.internship.internship.exeption.DataNotFoundException;
import com.internship.internship.mapper.GroupMapper;
import com.internship.internship.mapper.TaskMapper;
import com.internship.internship.model.Group;
import com.internship.internship.model.Task;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class GroupRepo {

    private static final Logger LOGGER = LoggerFactory.getLogger(GroupRepo.class);
    private final JdbcTemplate jdbcTemplate;
    private final NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    public GroupRepo(JdbcTemplate jdbcTemplate, NamedParameterJdbcTemplate namedParameterJdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
        this.namedParameterJdbcTemplate = namedParameterJdbcTemplate;
    }

    //
    public List<Group> getAll() {
        String sql = "select * from groupOfTasks g left join person p on p.id = g.id_person";
        List<Group> groups = jdbcTemplate.query(sql, new GroupMapper());
        for (Group group : groups) {
            group.setTasks(getTasksById(group.getId()));
        }
        return groups;
    }

    public Integer addGroup(MapSqlParameterSource parameters) {
        String sql = "insert into groupOfTasks ( id, name) values (:id, :name)";

        return namedParameterJdbcTemplate.update(sql, parameters);
    }

    public Integer updateGroup(Group group) {
        String sql = "update groupOfTasks set name = ? where id = ?;";

        return jdbcTemplate.update(sql, group.getName(), group.getId());
    }

    //
    public Integer deleteGroup(Long id) {
        String deleteConstrains = "delete from task_group where id_group = ?;";
        String deleteGroupSql = "delete from groupOfTasks where id = ?;";

        jdbcTemplate.update(deleteConstrains, id);

        return jdbcTemplate.update(deleteGroupSql, id);
    }

    public Integer addTaskToGroup(Long id, Task task) {
        String sql = "insert into task_group (id_group, id_task) values (?,?) ";
        return jdbcTemplate.update(sql, id, task.getId());
    }

    public Integer deleteTaskFromGroup(Long idGroup, Long idTask) {
        String sql = "delete from task_group where id_task = ? and id_group = ?";

        return jdbcTemplate.update(sql, idTask, idGroup);
    }

    //
    public Group getGroupById(Long id) {
        String sql = "select * from groupOfTasks g left join person p on p.id = g.id_person where g.id = ?";
        try {
            Group group = jdbcTemplate.queryForObject(sql, new GroupMapper(), id);
            group.setTasks(getTasksById(id));
            return group;
        } catch (EmptyResultDataAccessException exception) {
            LOGGER.warn("handling 404 error on getGroupById method");

            throw new DataNotFoundException(String.format("Group Id %d is not found", id));
        }
    }

    public List<Task> getTasksById(Long id) {
        String sqlForGroup = "select t.id, t.name, t.start_time, t.id_person,t.id_progress from groupOfTasks g " +
                "join task_group tg on g.id = tg.id_group " +
                "join task t on tg.id_task = t.id where g.id = ?";

        return jdbcTemplate.query(sqlForGroup, new TaskMapper(), id);
    }
}
