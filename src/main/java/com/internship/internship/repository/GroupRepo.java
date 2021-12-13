package com.internship.internship.repository;

import com.internship.internship.model.Group;
import com.internship.internship.model.Task;
import com.internship.internship.model.mapper.GroupMapper;
import com.internship.internship.model.mapper.TaskMapper;
import com.internship.internship.service.GroupService;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class GroupRepo {

    JdbcTemplate jdbcTemplate;
    GroupService groupService;

    public GroupRepo(JdbcTemplate jdbcTemplate, GroupService groupService) {
        this.jdbcTemplate = jdbcTemplate;
        this.groupService = groupService;
    }

    public Group getById(Long id) {
        String sql = "select * from groups g left join persons p on p.id = g.id_person " +
            "where g.id = ?";

        Group group = jdbcTemplate.queryForObject(sql,new GroupMapper(), id);

        group.setTasks(groupService.getTasksById(id));

        return group;
    }

    public List<Group> getAll() {
        String sql = "select * from groups g left join persons p on p.id = g.id_person";

        List<Group> groupsList = jdbcTemplate.query(sql, new GroupMapper());

        for (Group group: groupsList) {
            group.setTasks(groupService.getTasksById(group.getId()));
        }

        return groupsList;
    }

    public Integer add(Group group) {
        String sql = "insert into groups ( id, name, id_person) " +
            "values (?, ?, ?); " +
            "update persons set id_groups = null where id = ?; " +
            "update persons set id_groups = ? where id = ?;";

        Long personID = (group.getPerson() != null) ? group.getPerson().getId() : null;

        return jdbcTemplate.update(
            sql,
            group.getId(),
            group.getName(),
            personID,
            personID,
            group.getId(), personID
        );
    }

    public Integer update(Group group) {
        String sql = "update groups set id_person = ?, name = ? where id = ?;" +
            "update persons set id_groups = ? where id = ?";

        Long personID = (group.getPerson() != null) ? group.getPerson().getId() : null;

        return jdbcTemplate.update(sql,
            personID,
            group.getName(),
            group.getId(),
            group.getId(),
            personID);
    }

    public Integer delete(Long id) {
        String sql =
            "update persons set id_group = null where id = ?; " +
            "delete from tasks_groups where id_group = ?; " +
            "delete from groups where id = ?;";

        return jdbcTemplate.update(sql,id);
    }

    public Integer addTask(Long id, Task task) {
        String sql = "insert into tasks_groups (id_group, id_task) values (?,?) ";
        return jdbcTemplate.update(sql,id,task.getId());
    }

    public Integer deleteTask(Long id, Long idTask) {
        String sql = "delete from tasks_groups where id_task = ? and id_group = ?";
        return jdbcTemplate.update(sql,id,idTask);
    }
}
