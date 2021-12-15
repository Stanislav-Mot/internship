package com.internship.internship.repository;

import com.internship.internship.mapper.GroupMapper;
import com.internship.internship.mapper.TaskMapper;
import com.internship.internship.model.Group;
import com.internship.internship.model.Task;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class GroupRepo {

    private final JdbcTemplate jdbcTemplate;
    private final NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    public GroupRepo(JdbcTemplate jdbcTemplate, NamedParameterJdbcTemplate namedParameterJdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
        this.namedParameterJdbcTemplate = namedParameterJdbcTemplate;
    }

    public List<Group> getAll() {
        String sql = "select * from groups g left join persons p on p.id = g.id_person";

        List<Group> groupsList = jdbcTemplate.query(sql, new GroupMapper());

        return groupsList;
    }

    public Integer addGroup(MapSqlParameterSource parameters) {
        String sql = "insert into groups ( id, name) values (:id, :name)";

        return namedParameterJdbcTemplate.update(sql, parameters);
    }

    public Integer updateGroup(Group group) {
        String sql = "update groups name = ? where id = ?;";

        return jdbcTemplate.update(sql, group.getName(), group.getId());
    }

    public Integer deleteGroup(Long id) {
        String deleteConstrains = "delete from tasks_groups where id_group = ?;";
        String deleteGroupSql = "delete from groups where id = ?;";

        jdbcTemplate.update(deleteConstrains,id);

        return jdbcTemplate.update(deleteGroupSql,id);
    }

    public Integer addTaskToGroup(Long id, Task task) {
        String sql = "insert into tasks_groups (id_group, id_task) values (?,?) ";
        return jdbcTemplate.update(sql,id,task.getId());
    }

    public Integer deleteTaskFromGroup(Long id, Long idTask) {
        String sql = "delete from tasks_groups where id_task = ? and id_group = ?";
        return jdbcTemplate.update(sql,id,idTask);
    }

    public Group getGroupById(Long id) {
        String sql = "select * from groups g left join persons p on p.id = g.id_person " +
            "where g.id = ?";

        return jdbcTemplate.queryForObject(sql, new GroupMapper(),id);

    }

    public List<Task> getTasksById(Long id) {
        String sqlForGroup = "select * from groups g " +
            "join tasks_groups tg on g.id = tg.id_group " +
            "join tasks t on tg.id_task = t.id where g.id = ?";

        List<Task> tasksList = jdbcTemplate.query(sqlForGroup, new TaskMapper(), id);

        return tasksList;
    }
}
