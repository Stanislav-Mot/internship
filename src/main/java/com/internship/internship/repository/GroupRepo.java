package com.internship.internship.repository;

import com.internship.internship.model.Group;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class GroupRepo {

    JdbcTemplate jdbcTemplate;

    public GroupRepo(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public Group getById(Long id) {
        String sql = "select * from groups where group.id = ?";

        return jdbcTemplate.queryForObject(sql, (rs, rowNum) ->
            new Group(
                rs.getLong("id"),
                rs.getArray("id_task"),
                rs.getLong("id_person")
            ), id);
    }

    public List<Group> getAll() {
        String sql = "select * from groups";

        return jdbcTemplate.query(sql, (rs, rowNum) ->
            new Group(
                rs.getLong("id"),
                rs.getArray("id_task"),
                rs.getLong("id_person")
            )
        );    }

    public Integer add(Group tasksGroup) {
        String sql = "insert into groups ( id, id_person) " +
            "values (?,?)";

        return jdbcTemplate.update(
            sql,
            tasksGroup.getId(),
            tasksGroup.getPerson().getId()
        );    }

    public Integer update(Group tasksGroup) {
        String sql = "update groups set " +
            "id_person = ?, " +
            "where id = ?";

        return jdbcTemplate.update(sql,
            tasksGroup.getPerson().getId(),
            tasksGroup.getId());    }

    public Integer delete(Long id) {
        String sql = "delete from groups where id = ?";

        return jdbcTemplate.update(sql,id);    }
}
