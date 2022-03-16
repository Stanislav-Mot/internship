package com.internship.internship.service;

import com.internship.internship.dto.GroupDto;
import com.internship.internship.exeption.ChangesNotAppliedException;
import com.internship.internship.exeption.DataNotFoundException;
import com.internship.internship.mapper.GroupDtoMapper;
import com.internship.internship.mapper.TaskDtoMapper;
import com.internship.internship.model.AssignmentImpl;
import com.internship.internship.model.Group;
import com.internship.internship.model.Task;
import com.internship.internship.repository.GroupRepo;
import com.internship.internship.repository.TaskRepo;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
public class GroupService {
    private final GroupDtoMapper mapper;
    private final TaskDtoMapper taskDtoMapper;
    private final CacheService cacheService;
    private final GroupRepo groupRepo;
    private final TaskRepo taskRepo;

    public GroupService(GroupDtoMapper mapper, TaskDtoMapper taskDtoMapper, CacheService cacheService, GroupRepo groupRepo, TaskRepo taskRepo) {
        this.mapper = mapper;
        this.taskDtoMapper = taskDtoMapper;
        this.cacheService = cacheService;
        this.groupRepo = groupRepo;
        this.taskRepo = taskRepo;
    }

    public GroupDto getById(Long id) {
//        if (cacheService.isValid()) {
//            return (GroupDto) cacheService.getGroup(id);
//        } else {
//            Group group = groupRepo.findById(id).orElseThrow(() -> new DataNotFoundException(String.format("Group Id %d is not found", id)));
//            group.getPersons().forEach(x -> x.setGroups(null));
//            group.setTasks(getComposite(id));
//            return mapper.convertToDto(group);
//        }
        return null;
    }

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
//                group.setTasks(getComposite(group.getId()));
                return mapper.convertToDto(group);
            }).collect(Collectors.toList());
        }
    }

    public GroupDto add(GroupDto groupDto) {
        Group group = mapper.convertToEntity(groupDto);
        Group save = groupRepo.save(group);
        GroupDto dto = mapper.convertToDto(save);
//        cacheService.put(dto.getId(), Group.class, dto);
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
        if (!groupRepo.findById(id).isPresent() || !groupRepo.findById(groupId).isPresent()) {
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
        List<Group> groupList = /*groupRepo.findByGroupId(groupId);*/ null;
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
//        GroupDto groupDto = (GroupDto) cacheService.getGroup(group.getId());
//        cacheService.remove(id, Task.class);
//        return groupDto;
        return null;
    }

    private List<AssignmentImpl> getComposite(Long id) {
        List<Task> taskList = taskRepo.findByAssignmentsId(id);
//        List<AssignmentImpl> assignments = taskList.stream().map(task -> {
//            task.setGroups(null);
//            task.setPersons(null);
//            return taskDtoMapper.convertToDto(task);
//        }).collect(Collectors.toList());

//        List<Group> groupList = groupRepo.findByGroupId(id);
        List<Group> groupList = null;
        List<GroupDto> groupDtos = groupList.stream().map(mapper::convertToDto).collect(Collectors.toList());
        groupDtos.forEach(group -> {
            group.setPersons(null);
            group.setTasks(null);
            group.setTasks(getComposite(group.getId()));
        });

//        assignments.addAll(groupDtos);

        return /*assignments*/ null;
    }
}
