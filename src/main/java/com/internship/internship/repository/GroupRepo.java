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

    public List<Group> getAll() {
        String sql = "select * from groups g left join persons p on p.id = g.id_person";

        return jdbcTemplate.query(sql, new GroupMapper());
    }

    public Integer addGroup(MapSqlParameterSource parameters) {
        String sql = "insert into groups ( id, name, id_person) values (:id, :name, :id_person)";

        return namedParameterJdbcTemplate.update(sql, parameters);
    }

    public Integer updateGroup(Group group) {
        String sql = "update groups set name = ? where id = ?;";

        return jdbcTemplate.update(sql, group.getName(), group.getId());
    }

    public Integer deleteGroup(Long id) {
        String deleteConstrains = "delete from tasks_groups where id_group = ?;";
        String deleteGroupSql = "delete from groups where id = ?;";

        jdbcTemplate.update(deleteConstrains, id);

        return jdbcTemplate.update(deleteGroupSql, id);
    }

    public Integer addTaskToGroup(Long id, Task task) {
        String sql = "insert into tasks_groups (id_group, id_task) values (?,?) ";
        return jdbcTemplate.update(sql, id, task.getId());
    }

    public Integer deleteTaskFromGroup(Long idGroup, Long idTask) {
        String sql = "delete from tasks_groups where id_task = ? and id_group = ?";

        return jdbcTemplate.update(sql, idTask, idGroup);
    }

    public Group getGroupById(Long id) {
        String sql = "select * from groups g left join persons p on p.id = g.id_person where g.id = ?";
        try {
            return jdbcTemplate.queryForObject(sql, new GroupMapper(), id);
        } catch (EmptyResultDataAccessException exception) {
            LOGGER.debug("handling 404 error on getGroupById method");

            throw new DataNotFoundException(String.format("Group Id %d is not found", id));
        }
    }

    public List<Task> getTasksById(Long id) {
        String sqlForGroup = "select t.id, t.name, t.start_time, t.id_person,t.id_progress from groups g " +
            "join tasks_groups tg on g.id = tg.id_group " +
            "join tasks t on tg.id_task = t.id where g.id = ?";

        return jdbcTemplate.query(sqlForGroup, new TaskMapper(), id);
    }
}
