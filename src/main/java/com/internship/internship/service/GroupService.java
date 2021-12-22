package com.internship.internship.service;

import com.internship.internship.model.Group;
import com.internship.internship.model.Task;
import com.internship.internship.repository.GroupRepo;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class GroupService {

    private final GroupRepo groupRepo;

    public GroupService(GroupRepo groupRepo) {
        this.groupRepo = groupRepo;
    }

    public Group getById(Long id) {
        Group group = groupRepo.getGroupById(id);

        group.setTasks(groupRepo.getTasksById(id)); //  все сборки объекта перенеси в репозиторий

        return group;
    }

    public List<Group> getAll() {
        List<Group> groupsList = groupRepo.getAll();

        for (Group group : groupsList) {
            group.setTasks(groupRepo.getTasksById(group.getId()));
        }

        return groupsList;
    }

    public Integer add(Group group) {
        MapSqlParameterSource parameters = new MapSqlParameterSource();
        parameters.addValue("id", group.getId());
        parameters.addValue("name", group.getName());
        return groupRepo.addGroup(parameters);
    }

    public Integer update(Group group) {
        return groupRepo.updateGroup(group);
    }

    public Integer delete(Long id) {
        return groupRepo.deleteGroup(id);
    }

    public Integer addTask(Long id, Task task) {
        return groupRepo.addTaskToGroup(id, task);
    }

    public Integer deleteTask(Long id, Long taskId) {
        return groupRepo.deleteTaskFromGroup(id, taskId);
    }
}
