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
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class TaskRepo {

    private final JdbcTemplate jdbcTemplate;
    private final NamedParameterJdbcTemplate namedParameterJdbcTemplate;
    private static final Logger LOGGER = LoggerFactory.getLogger(GroupRepo.class);

    public TaskRepo(JdbcTemplate jdbcTemplate, NamedParameterJdbcTemplate namedParameterJdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
        this.namedParameterJdbcTemplate = namedParameterJdbcTemplate;
    }

    public Task getTaskById(Long id) {
        String sql = "select * from tasks t " +
                "left join persons p on p.id = t.id_person " +
                "left join progresses pr on pr.id = t.id_progress " +
                "where t.id = ?";
        Task task;
        try {
            task = jdbcTemplate.queryForObject(sql, new TaskMapper(), id);
        }
        catch (EmptyResultDataAccessException exception) {
            LOGGER.debug("handling 404 error on getTaskById method");

            throw new DataNotFoundException(String.format("Task Id %d is not found", id));
        }
        return task;
    }

    public List<Task> getAllTasks() {
        String sql = "select * from tasks t " +
                "left join persons p on p.id = t.id_person " +
                "left join progresses pr on pr.id = t.id_progress";

        return jdbcTemplate.query(sql, new TaskMapper());
    }

    public Integer addTask(SqlParameterSource parameters) {
        String sql = "insert into tasks (id, name, start_time, id_person, id_progress) " +
                "values (:id, :name, :date, :personId, :progressId);" +
                "update progresses set id_task = :id where id = progressId";

        return namedParameterJdbcTemplate.update(sql, parameters);
    }

    public Integer updateTask(SqlParameterSource parameters) {
        String sql = "update tasks set name = :name," +
                "start_time = :date, id_person = :personId, " +
                "id_progress = :progressId where id = :id";

        return namedParameterJdbcTemplate.update(sql, parameters);
    }

    public Integer deleteTask(Long id) {
        String sql = "update progresses set id_task = null where id = ?; " +
                "delete from tasks_groups where id_task = ?; " +
                "delete from tasks where id = ?;";

        return jdbcTemplate.update(sql, id, id, id);
    }

    public Integer addGroupToTask(Long id, Group group) {
        String sql = "insert into tasks_groups (id_task, id_group) values (?,?) ";
        return jdbcTemplate.update(sql, id, group.getId());
    }

    public Integer deleteGroupFromTask(Long id, Long idGroup) {
        String sql = "delete from tasks_groups where id_task = ? and id_group = ?";
        return jdbcTemplate.update(sql, id, idGroup);
    }

    public List<Group> getGroupsById(Long id) {
        String sqlForGroup = "select * from tasks t join tasks_groups tg on t.id = tg.id_task " +
                "join groups g on tg.id_group = g.id where t.id = ?";

        return jdbcTemplate.query(sqlForGroup, new GroupMapper(), id);
    }
}
