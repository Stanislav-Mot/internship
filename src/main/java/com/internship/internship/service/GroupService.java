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
    public static final String MESSAGE = "id: %d is not found";
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
        cacheService.setInvalid(save.getId(), Group.class);
        return groupMapper.convertToDto(save);
    }

    @Transactional
    public GroupDto addTask(Long groupId, Long taskId) {
        Group group = groupRepo.findById(groupId).orElseThrow(() ->
                new ChangesNotAppliedException(String.format(MESSAGE, groupId)));
        Task task = taskRepo.findById(taskId).orElseThrow(() ->
                new ChangesNotAppliedException(String.format(MESSAGE, taskId)));

        group.getTasks().add(task);
        Group updated = groupRepo.save(group);

        task.setStartTime(LocalDateTime.now());
        taskRepo.save(task);

        cacheService.setInvalid(groupId, Group.class);
        cacheService.setInvalid(taskId, Task.class);
        return groupMapper.convertToDto(updated);
    }

    public void deleteTask(Long groupId, Long taskId) {
        Group group = groupRepo.findById(groupId).orElseThrow(() ->
                new ChangesNotAppliedException(String.format(MESSAGE, groupId)));
        Task task = taskRepo.findById(taskId).orElseThrow(() ->
                new ChangesNotAppliedException(String.format(MESSAGE, taskId)));

        group.getTasks().remove(task);
        groupRepo.save(group);

        cacheService.setInvalid(groupId, Group.class);
        cacheService.setInvalid(taskId, Task.class);
    }

    @Transactional
    public GroupDto addGroup(Long groupId, Long groupIdForAdd) {
        Group group = groupRepo.findById(groupId).orElseThrow(() ->
                new ChangesNotAppliedException(String.format(MESSAGE, groupId)));
        Group children = groupRepo.findById(groupIdForAdd).orElseThrow(() ->
                new ChangesNotAppliedException(String.format(MESSAGE, groupIdForAdd)));

        if (Objects.equals(group.getId(), children.getId())) {
            throw new ChangesNotAppliedException("group cannot refer to itself");
        }
        if (checkCyclicDependency(group.getId(), children.getId())) {
            throw new ChangesNotAppliedException("cyclic dependency");
        }
        group.getChildren().add(children);
        cacheService.setInvalid(groupId, Group.class);
        return groupMapper.convertToDto(group);
    }

    public void deleteGroup(Long groupId, Long groupIdForDelete) {
        Group group = groupRepo.findById(groupId).orElseThrow(() ->
                new ChangesNotAppliedException(String.format(MESSAGE, groupId)));
        Group children = groupRepo.findById(groupIdForDelete).orElseThrow(() ->
                new ChangesNotAppliedException(String.format(MESSAGE, groupIdForDelete)));

        group.getChildren().remove(children);
        cacheService.setInvalid(groupId, Group.class);
    }

    private boolean checkCyclicDependency(Long id, Long groupId) {
        List<Long> groupList = groupRepo.getNextGeneration(groupId);
        return groupList.stream().anyMatch(id::equals);
    }

    public void delete(Long id) {
        Group group = groupRepo.findById(id).orElseThrow(() ->
                new ChangesNotAppliedException(String.format(MESSAGE, id)));

        groupRepo.deleteById(group.getId());
        cacheService.remove(id, Group.class);
    }
}
