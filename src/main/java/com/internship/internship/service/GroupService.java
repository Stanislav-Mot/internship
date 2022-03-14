package com.internship.internship.service;

import com.internship.internship.dto.GroupDto;
import com.internship.internship.exeption.ChangesNotAppliedException;
import com.internship.internship.exeption.DataNotFoundException;
import com.internship.internship.mapper.GroupDtoMapper;
import com.internship.internship.model.Assignment;
import com.internship.internship.model.Group;
import com.internship.internship.model.Task;
import com.internship.internship.repository.GroupRepo;
import com.internship.internship.repository.PersonRepo;
import com.internship.internship.repository.TaskRepo;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
public class GroupService {
    private final GroupDtoMapper mapper;
    private final CacheService cacheService;
    private final GroupRepo groupRepo;
    private final TaskRepo taskRepo;
    private final PersonRepo personRepo;

    public GroupService(GroupDtoMapper mapper, CacheService cacheService, GroupRepo groupRepo, TaskRepo taskRepo, PersonRepo personRepo) {
        this.mapper = mapper;
        this.cacheService = cacheService;
        this.groupRepo = groupRepo;
        this.taskRepo = taskRepo;
        this.personRepo = personRepo;
    }

    public GroupDto getById(Long id) {
        if (cacheService.isValid()) {
            return (GroupDto) cacheService.getGroup(id);
        } else {
            Group group = groupRepo.findById(id).orElseThrow(() -> new DataNotFoundException(String.format("Group Id %d is not found", id)));
            group.setTasks(getComposite(id));
            return mapper.convertToDto(group);
        }
    }

    //
    public List<GroupDto> getByPersonId(Long id) {
        return groupRepo.findByPersonsId(id)
                .stream().map(mapper::convertToDto)
                .collect(Collectors.toList());
    }

    public List<GroupDto> getAll() {
        if (cacheService.isValid()) {
            return cacheService.getAllGroup();
        } else {
            return groupRepo.findAll().stream().map(group -> {
                group.setTasks(getComposite(group.getId()));
                return mapper.convertToDto(group);
            }).collect(Collectors.toList());
        }
    }

    public GroupDto add(GroupDto groupDto) {
        Group group = mapper.convertToEntity(groupDto);
        Group save = groupRepo.save(group);
        GroupDto dto = mapper.convertToDto(save);
        cacheService.put(dto.getId(), Group.class, dto);
        return dto;
    }

    public GroupDto update(GroupDto groupDto) {
        Group group = mapper.convertToEntity(groupDto);
        Group save = groupRepo.save(group);
        cacheService.setValid(false);
        return mapper.convertToDto(save);
    }

    public GroupDto addTask(Long id, Long taskId) {
        if (!taskRepo.findById(taskId).isPresent() || !groupRepo.findById(id).isPresent()) {
            throw new ChangesNotAppliedException(String.format("id: %d or %d is not found", id, taskId));
        }
        groupRepo.addTaskToGroup(id, taskId);
        taskRepo.setStartTime(taskId);

        cacheService.setValid(false);
        return getById(id);
    }

    public void deleteTask(Long id, Long taskId) {
        Integer answer = groupRepo.deleteTaskFromGroup(id, taskId);
        if (answer < 1) {
            throw new ChangesNotAppliedException(String.format("Group Id %d or Task id %d is not found", id, taskId));
        }
        cacheService.setValid(false);
    }

    public GroupDto addGroup(Long id, Long groupId) {
        if (Objects.equals(id, groupId)) {
            throw new ChangesNotAppliedException("group cannot refer to itself");
        }
        if (groupRepo.findById(id).isPresent() && groupRepo.findById(groupId).isPresent()) {
            throw new ChangesNotAppliedException(String.format("Group with id: %d or %d is not found", id, groupId));
        }
        if (checkCyclicDependency(id, groupId)) {
            throw new ChangesNotAppliedException("cyclic dependency");
        }
        groupRepo.addGroupToGroup(id, groupId);
        cacheService.setValid(false);
        return getById(id);
    }

    private boolean checkCyclicDependency(Long id, Long groupId) {
        List<Group> groupList = groupRepo.findByGroupId(groupId);
        if (groupList.isEmpty()) {
            return false;
        }
        for (Group group : groupList) {
            if (Objects.equals(group.getId(), id)) {
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
            throw new ChangesNotAppliedException(String.format("Group Id %d or %d is not found", id, groupId));
        }
        cacheService.setValid(false);
    }

    public GroupDto delete(Long id) {
        Group group = groupRepo.findById(id).orElseThrow(() -> new ChangesNotAppliedException(String.format("Group id: %d is not found", id)));
        groupRepo.deleteById(id);
        GroupDto groupDto = (GroupDto) cacheService.getGroup(group.getId());
        cacheService.remove(id, Task.class);
        return groupDto;
    }

    private List<Assignment> getComposite(Long id) {
        List<Task> taskList = taskRepo.findByGroupsId(id);
        List<Assignment> assignments = new ArrayList<>(taskList);

        List<Group> groupList = groupRepo.findByGroupId(id);
        groupList.forEach(group -> group.setTasks(getComposite(group.getId())));

        assignments.addAll(groupList);

        return assignments;
    }
}
