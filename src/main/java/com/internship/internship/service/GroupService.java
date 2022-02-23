package com.internship.internship.service;

import com.internship.internship.cache.ACache;
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
    private final ACache aCache;

    public GroupService(GroupRepo groupRepo, GroupDtoMapper mapper, TaskRepo taskRepo, ACache aCache) {
        this.groupRepo = groupRepo;
        this.mapper = mapper;
        this.taskRepo = taskRepo;
        this.aCache = aCache;
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
                .stream().map(mapper::convertToDto)
                .collect(Collectors.toList());
    }

    public List<GroupDto> getAll() {
        return groupRepo.getAll()
                .stream().map(mapper::convertToDto)
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

    public GroupDto addTask(Long id, Long taskId) {
        if (taskRepo.getTaskById(taskId) == null || groupRepo.getGroupById(id) == null) {
            throw new ChangesNotAppliedExemption(String.format("id: %d or %d is not found", id, taskId));
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
        if (id == groupId) {
            throw new ChangesNotAppliedExemption("group cannot refer to itself");
        }

        Group inGroup = groupRepo.getGroupById(id);
        Group fromGroup = groupRepo.getGroupById(groupId);

        if (inGroup == null || fromGroup == null) {
            throw new ChangesNotAppliedExemption(String.format("Group with id: %d or %d is not found", id, groupId));
        }

        if (checkCyclicDependency(id, groupId)) {
            throw new ChangesNotAppliedExemption("cyclic dependency");
        }

        Group group = groupRepo.addGroupToGroup(id, groupId);
        return mapper.convertToDto(group);
    }

    private boolean checkCyclicDependency(Long id, Long groupId) {
        List<Group> groupList = groupRepo.getAllGroupInGroup(groupId);
        if (groupList.isEmpty()) {
            return false;
        }
        for (Group group : groupList) {
            if (group.getId() == id) {
                return true;
            } else {
                if (checkCyclicDependency(id, group.getId())) {
                    return true;
                }
            }
        }
        return false;
    }

    public void deleteGroup(Long id, Long groupId) {
        Integer answer = groupRepo.deleteGroupFromGroup(id, groupId);
        if (answer < 1) {
            throw new ChangesNotAppliedExemption(String.format("Group Id %d or %d is not found", id, groupId));
        }
    }

    public GroupDto delete(Long id) {
        GroupDto groupDto;

        Integer answer = groupRepo.delete(id);
        if (answer < 1) {
            throw new ChangesNotAppliedExemption(String.format("Task id: %d is not found", id));
        } else {
            groupDto = (GroupDto) aCache.get(id);
        }
        return groupDto;
    }
}
