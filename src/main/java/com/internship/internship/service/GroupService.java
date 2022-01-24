package com.internship.internship.service;

import com.internship.internship.dto.GroupDto;
import com.internship.internship.exeption.ChangesNotAppliedExemption;
import com.internship.internship.mapper.GroupDtoMapper;
import com.internship.internship.model.Group;
import com.internship.internship.repository.GroupRepo;
import com.internship.internship.repository.TaskRepo;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class GroupService {

    private final GroupRepo groupRepo;
    private final TaskRepo taskRepo;
    private final GroupDtoMapper mapper;

    public GroupService(GroupRepo groupRepo, GroupDtoMapper mapper, TaskRepo taskRepo) {
        this.groupRepo = groupRepo;
        this.mapper = mapper;
        this.taskRepo = taskRepo;
    }

    private MapSqlParameterSource getMapSqlParameterSource(GroupDto groupDto) {
        MapSqlParameterSource parameters = new MapSqlParameterSource();
        parameters.addValue("id", groupDto.getId());
        parameters.addValue("name", groupDto.getName());
        return parameters;
    }

    public GroupDto getById(Long id) {
        return mapper.convertToDto(groupRepo.getGroupById(id));
    }

    public List<GroupDto> getByPersonId(Long id) {
        return groupRepo.getByPersonId(id)
                .stream().map(group -> mapper.convertToDto(group))
                .collect(Collectors.toList());
    }

    public List<GroupDto> getAll() {
        return groupRepo.getAll()
                .stream().map(group -> mapper.convertToDto(group))
                .collect(Collectors.toList());
    }

    public GroupDto add(GroupDto groupDto) {
        MapSqlParameterSource parameters = getMapSqlParameterSource(groupDto);

        KeyHolder holder = groupRepo.addGroup(parameters);

        return mapper.getDtoFromHolder(holder);
    }

    public GroupDto update(GroupDto groupDto) {
        Group group = mapper.convertToEntity(groupDto);
        Group response = groupRepo.updateGroup(group);

        return mapper.convertToDto(response);
    }

    public Integer delete(Long id) {
        return groupRepo.deleteGroup(id);
    }

    public GroupDto addTask(Long id, Long taskId) {
        if (taskRepo.getTaskById(taskId) == null) {
            throw new ChangesNotAppliedExemption(String.format("Task with id: %d is not found", taskId));
        }
        if (groupRepo.getGroupById(id) == null) {
            throw new ChangesNotAppliedExemption(String.format("Group with id: %d is not found", id));
        }
        Group group = groupRepo.addTaskToGroup(id, taskId);
        if (group != null) {
            taskRepo.setStartTime(taskId);
        }
        return mapper.convertToDto(group);
    }

    public void deleteTask(Long id, Long taskId) {
        Integer answer = groupRepo.deleteTaskFromGroup(id, taskId);
        if (answer < 1) {
            throw new ChangesNotAppliedExemption(String.format("Group Id %d or Task id %d is not found", id, taskId));
        }
    }

    public GroupDto addGroup(Long id, Long groupId) {
        if (groupRepo.getGroupById(groupId) == null) {
            throw new ChangesNotAppliedExemption(String.format("Group with id: %d is not found", groupId));
        }
        if (groupRepo.getGroupById(id) == null) {
            throw new ChangesNotAppliedExemption(String.format("Group with id: %d is not found", id));
        }
        Group group = groupRepo.addGroupToGroup(id, groupId);
        return mapper.convertToDto(group);
    }

    public void deleteGroup(Long id, Long groupId) {
        Integer answer = groupRepo.deleteGroupFromGroup(id, groupId);
        if (answer < 1) {
            throw new ChangesNotAppliedExemption(String.format("Group Id %d or %d is not found", id, groupId));
        }
    }
}
