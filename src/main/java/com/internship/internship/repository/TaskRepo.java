package com.internship.internship.repository;

import com.internship.internship.model.Group;
import com.internship.internship.model.Task;
import com.internship.internship.mapper.TaskMapper;
import com.internship.internship.service.TaskService;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.sql.Date;
import java.util.List;

@Repository
public class TaskRepo {

    private final JdbcTemplate jdbcTemplate;
    private final TaskService taskService;

    public TaskRepo(JdbcTemplate jdbcTemplate, TaskService taskService) {
        this.jdbcTemplate = jdbcTemplate;
        this.taskService = taskService;
    }

    public Task getById(Long id) {
        String sql =
            "select * from tasks t " +
                "left join persons p on p.id = t.id_person " +
                "left join progresses pr on pr.id = t.id_progress " +
                "where t.id = ?";

        Task task = jdbcTemplate.queryForObject(sql, new TaskMapper(), id);

        task.setGroupsList(taskService.getGroupsById(id));

        return task;
    }

    public List<Task> getAll() {
        String sql =
            "select * from tasks t " +
                "left join persons p on p.id = t.id_person " +
                "left join progresses pr on pr.id = t.id_progress";

        List<Task> taskList = jdbcTemplate.query(sql, new TaskMapper());

        for (Task task : taskList) {
            task.setGroupsList(taskService.getGroupsById(task.getId()));
        }

        return taskList;
    }

    public Integer add(Task task) {
        String sql =
            "insert into tasks (id, name, start_time, id_person, id_progress) " +
                "values (?,?,?,?,?);" +
                "update progresses set id_task = null where id = ?;" +
                "update progresses set id_task = ? where id = ?";

        Long personID = (task.getPerson() != null) ? task.getPerson().getId() : null;
        Long progressID = (task.getProgress() != null) ? task.getProgress().getId() : null;
        Date date = (task.getStartTime() != null) ? Date.valueOf(task.getStartTime()) : null;

        return jdbcTemplate.update(
            sql,
            task.getId(), task.getName(), date, personID, progressID,
            progressID,
            task.getId(), progressID
        );
    }

    public Integer update(Task task) {
        String sql =
            "update tasks set name = ?," +
                "start_time = ?, " +
                "id_person = ?, " +
                "id_progress = ? " +
                "where id = ?";

        Long personID = (task.getPerson() != null) ? task.getPerson().getId() : null;
        Long progressID = (task.getProgress() != null) ? task.getProgress().getId() : null;
        Date date = (task.getStartTime() != null) ? Date.valueOf(task.getStartTime()) : null;

        return jdbcTemplate.update(sql,
            task.getName(),
            date,
            personID,
            progressID,
            task.getId());
    }

    public Integer delete(Long id) {
        String sql =
            "update progresses set id_task = null where id = ?; " +
                "delete from tasks_groups where id_task = ?; " +
                "delete from tasks where id = ?;";

        return jdbcTemplate.update(sql, id, id, id);
    }

    public Integer addGroup(Long id, Group group) {
        String sql = "insert into tasks_groups (id_task, id_group) values (?,?) ";
        return jdbcTemplate.update(sql, id, group.getId());
    }

    public Integer deleteGroup(Long id, Long idGroup) {
        String sql = "delete from tasks_groups where id_task = ? and id_group = ?";
        return jdbcTemplate.update(sql, id, idGroup);
    }
}
