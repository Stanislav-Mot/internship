package com.internship.internship.service;

import com.internship.internship.dto.GroupDto;
import com.internship.internship.mapper.GroupDtoMapper;
import com.internship.internship.mapper.TaskDtoMapper;
import com.internship.internship.model.Group;
import com.internship.internship.repository.GroupRepo;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class GroupService {

    private final GroupRepo groupRepo;
    private final GroupDtoMapper mapper;

    public GroupService(GroupRepo groupRepo, GroupDtoMapper mapper) {
        this.groupRepo = groupRepo;
        this.mapper = mapper;
    }

    private static MapSqlParameterSource getMapSqlParameterSource(GroupDto groupDto) {
        MapSqlParameterSource parameters = new MapSqlParameterSource();
        parameters.addValue("id", groupDto.getId());
        parameters.addValue("name", groupDto.getName());
        return parameters;
    }

    public GroupDto getById(Long id) {
        return mapper.convertToDto(groupRepo.getGroupById(id));
    }

    public List<GroupDto> getByPersonId(Long id) {
        List<Group> groups = groupRepo.getByPersonId(id);

        List<GroupDto> groupsDto = new ArrayList<>();
        for (Group group : groups) {
            groupsDto.add(mapper.convertToDto(group));
        }
        return groupsDto;
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

        MapSqlParameterSource parameters = getMapSqlParameterSource(groupDto);
        return groupRepo.addGroup(parameters);
    }

    public Integer update(GroupDto groupDto) {
        Group group = mapper.convertToEntity(groupDto);
        return groupRepo.updateGroup(group);
    }

    public Integer delete(Long id) {
        return groupRepo.deleteGroup(id);
    }

    public Integer addTask(Long id, Long taskId) {
        return groupRepo.addTaskToGroup(id, taskId);
    }

    public Integer deleteTask(Long id, Long taskId) {
        return groupRepo.deleteTaskFromGroup(id, taskId);
    }

    public Integer addGroup(Long id, Long groupId) {
        return groupRepo.addGroupToGroup(id, groupId);
    }

    public Integer deleteGroup(Long id, Long groupId) {
        return groupRepo.deleteGroupFromGroup(id, groupId);
    }

}
