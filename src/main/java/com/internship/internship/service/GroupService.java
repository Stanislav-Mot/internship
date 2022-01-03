package com.internship.internship.service;

import com.internship.internship.dto.GroupDto;
import com.internship.internship.dto.TaskDto;
import com.internship.internship.mapper.GroupDtoMapper;
import com.internship.internship.mapper.TaskDtoMapper;
import com.internship.internship.model.Group;
import com.internship.internship.model.Task;
import com.internship.internship.repository.GroupRepo;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class GroupService {

    private final GroupRepo groupRepo;
    private final GroupDtoMapper mapper;
    private final TaskDtoMapper taskDtoMapper;

    public GroupService(GroupRepo groupRepo, GroupDtoMapper mapper, TaskDtoMapper taskDtoMapper) {
        this.groupRepo = groupRepo;
        this.mapper = mapper;
        this.taskDtoMapper = taskDtoMapper;
    }

    public GroupDto getById(Long id) {
        Group group = groupRepo.getGroupById(id);
        GroupDto groupDto = mapper.convertToDto(group);
        return groupDto;
    }

    public List<GroupDto> getAll() {
        List<Group> groups = groupRepo.getAll();

        List<GroupDto> groupsDto = new ArrayList<>();
        for (Group group : groups) {
            groupsDto.add(mapper.convertToDto(group));
        }
        return groupsDto;
    }

    public Integer add(GroupDto groupDto) {

        MapSqlParameterSource parameters = new MapSqlParameterSource();
        parameters.addValue("id", groupDto.getId());
        parameters.addValue("name", groupDto.getName());
        return groupRepo.addGroup(parameters);
    }

    public Integer update(GroupDto groupDto) {
        Group group = mapper.convertToEntity(groupDto);
        return groupRepo.updateGroup(group);
    }

    public Integer delete(Long id) {
        return groupRepo.deleteGroup(id);
    }

    public Integer addTask(Long id, TaskDto taskDto) {
        Task task = taskDtoMapper.convertToEntity(taskDto);
        return groupRepo.addTaskToGroup(id, task);
    }

    public Integer deleteTask(Long id, Long taskId) {
        return groupRepo.deleteTaskFromGroup(id, taskId);
    }
}
