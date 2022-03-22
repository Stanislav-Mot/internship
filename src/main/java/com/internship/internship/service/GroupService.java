package com.internship.internship.service;

import com.internship.internship.dto.GroupDto;
import com.internship.internship.exeption.ChangesNotAppliedException;
import com.internship.internship.mapper.GroupMapper;
import com.internship.internship.model.Group;
import com.internship.internship.model.Task;
import com.internship.internship.repository.GroupRepo;
import com.internship.internship.repository.TaskRepo;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
public class GroupService {
    private final GroupMapper groupMapper;
    private final CacheService cacheService;
    private final GroupRepo groupRepo;
    private final TaskRepo taskRepo;

    public GroupService(GroupMapper groupMapper, CacheService cacheService, GroupRepo groupRepo, TaskRepo taskRepo) {
        this.groupMapper = groupMapper;
        this.cacheService = cacheService;
        this.groupRepo = groupRepo;
        this.taskRepo = taskRepo;
    }

    public GroupDto getById(Long id) {
        return (GroupDto) cacheService.getGroup(id);
    }

    public List<GroupDto> getByPersonId(Long id) {
        return cacheService.findByPersonId(id).stream().map(GroupDto.class::cast).collect(Collectors.toList());
    }

    public List<GroupDto> getAll() {
        return cacheService.getAllGroup();
    }

    public GroupDto add(GroupDto groupDto) {
        Group group = groupMapper.convertToEntity(groupDto);
        Group save = groupRepo.save(group);
        GroupDto saveDto = groupMapper.convertToDto(save);
        cacheService.addAssignment(saveDto, Group.class);
        return saveDto;
    }

    public GroupDto update(GroupDto groupDto) {
        Group group = groupMapper.convertToEntity(groupDto);
        Group save = groupRepo.save(group);
        cacheService.setInvalid(save.getId(), Task.class);
        return groupMapper.convertToDto(save);
    }

    @Transactional
    public GroupDto addTask(Long groupId, Long taskId) {
        Group group = groupRepo.findById(groupId).orElseThrow(() ->
                new ChangesNotAppliedException(String.format("Group id: %d is not found", groupId)));
        Task task = taskRepo.findById(taskId).orElseThrow(() ->
                new ChangesNotAppliedException(String.format("Task id: %d is not found", taskId)));

        group.getTasks().add(task);
        Group updated = groupRepo.save(group);

        task.setStartTime(LocalDateTime.now());
        taskRepo.save(task);

        cacheService.setInvalid(groupId, Group.class);
        cacheService.setInvalid(taskId, Task.class);
        return groupMapper.convertToDto(updated);
    }

    public GroupDto deleteTask(Long groupId, Long taskId) {
        Group group = groupRepo.findById(groupId).orElseThrow(() ->
                new ChangesNotAppliedException(String.format("Group id: %d is not found", groupId)));
        Task task = taskRepo.findById(taskId).orElseThrow(() ->
                new ChangesNotAppliedException(String.format("Task id: %d is not found", taskId)));

        group.getTasks().remove(task);
        Group updated = groupRepo.save(group);

        cacheService.setInvalid(groupId, Group.class);
        cacheService.setInvalid(taskId, Task.class);
        return groupMapper.convertToDto(updated);
    }

    @Transactional
    public GroupDto addGroup(Long groupId, Long groupIdForAdd) {
        Group group = groupRepo.findById(groupId).orElseThrow(() ->
                new ChangesNotAppliedException(String.format("id: %d is not found", groupId)));
        Group children = groupRepo.findById(groupIdForAdd).orElseThrow(() ->
                new ChangesNotAppliedException(String.format("id: %d is not found", groupIdForAdd)));

        if (Objects.equals(group.getId(), children.getId())) {
            throw new ChangesNotAppliedException("group cannot refer to itself");
        }
        if (checkCyclicDependency(group.getId(), children)){
            throw new ChangesNotAppliedException("cyclic dependency");
        }
        group.getChildren().add(children);
        cacheService.setInvalid(groupId, Group.class);
        return groupMapper.convertToDto(group);
    }

    @Transactional
    public GroupDto deleteGroup(Long groupId, Long groupIdForDelete) {
        Group group = groupRepo.findById(groupId).orElseThrow(() ->
                new ChangesNotAppliedException(String.format("id: %d is not found", groupId)));
        Group children = groupRepo.findById(groupIdForDelete).orElseThrow(() ->
                new ChangesNotAppliedException(String.format("id: %d is not found", groupIdForDelete)));

        group.getChildren().remove(children);
        cacheService.setInvalid(groupId, Group.class);
        return groupMapper.convertToDto(group);
    }

    private boolean checkCyclicDependency(Long id, Group groupId) {
        List<Group> groupList = groupRepo.findByAssignments(groupId);
        if (groupList.isEmpty()) {
            return false;
        }
        for (Group group : groupList) {
            if (Objects.equals(group.getId(), id)) {
                return true;
            } else {
                if (checkCyclicDependency(id, group)) {
                    return true;
                }
            }
        }
        return false;
    }

    public void delete(Long id) {
        Group group = groupRepo.findById(id).orElseThrow(() ->
                new ChangesNotAppliedException(String.format("id: %d is not found", id)));

        groupRepo.deleteById(group.getId());
        cacheService.remove(id, Group.class);
    }
}
