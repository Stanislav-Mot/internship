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

        group.setTasks(groupRepo.getTasksById(id));

        return group;
    }

    public List<Group> getAll() {
        List<Group> groupsList = groupRepo.getAll();

        for (Group group: groupsList) {
            group.setTasks(groupRepo.getTasksById(group.getId()));
        }

        return groupsList;
    }

    public Integer add(Group group) {
        MapSqlParameterSource parameters = new MapSqlParameterSource();
        parameters.addValue("id", group.getId());
        parameters.addValue("name", group.getName());

        Integer answer = groupRepo.addGroup(parameters);

        return answer;
    }


    public Integer update(Group group) {
        Integer answer = groupRepo.updateGroup(group);

        return  answer;
    }

    public Integer delete(Long id) {
        Integer answer = groupRepo.deleteGroup(id);

        return answer;
    }

    public Integer addTask(Long id, Task task) {
        Integer answer = groupRepo.addTaskToGroup(id, task);

        return answer;
    }

    public Integer deleteTask(Long id, Long taskId) {
        Integer answer = groupRepo.deleteTaskFromGroup(id, taskId);

        return answer;
    }
}
