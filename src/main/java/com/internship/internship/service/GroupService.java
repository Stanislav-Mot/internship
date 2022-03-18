package com.internship.internship.service;

import com.internship.internship.dto.GroupDto;
import com.internship.internship.exeption.ChangesNotAppliedException;
import com.internship.internship.exeption.DataNotFoundException;
import com.internship.internship.mapper.GroupMapper;
import com.internship.internship.mapper.TaskMapper;
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
    private final TaskMapper taskMapper;
    private final CacheService cacheService;
    private final GroupRepo groupRepo;
    private final TaskRepo taskRepo;

    public GroupService(GroupMapper groupMapper, TaskMapper taskMapper, CacheService cacheService, GroupRepo groupRepo, TaskRepo taskRepo) {
        this.groupMapper = groupMapper;
        this.taskMapper = taskMapper;
        this.cacheService = cacheService;
        this.groupRepo = groupRepo;
        this.taskRepo = taskRepo;
    }

    @Transactional
    public GroupDto getById(Long id) {
        Group group = groupRepo.findById(id).orElseThrow(() -> new DataNotFoundException(String.format("Group Id %d is not found", id)));
        return groupMapper.convertToDto(group);
    }

    public List<GroupDto> getByPersonId(Long id) {
        return groupRepo.findByPersonId(id)
                .stream().map(groupMapper::convertToDto)
                .collect(Collectors.toList());
    }

    public List<GroupDto> getAll() {
        List<Group> all = groupRepo.findAll();
        return all.stream().map(groupMapper::convertToDto).collect(Collectors.toList());

    }

    public GroupDto add(GroupDto groupDto) {
        Group group = groupMapper.convertToEntity(groupDto);
        Group save = groupRepo.save(group);
        return groupMapper.convertToDto(save);
    }

    public GroupDto update(GroupDto groupDto) {
        Group group = groupMapper.convertToEntity(groupDto);
        Group save = groupRepo.save(group);
        cacheService.setValid(false);
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

        return groupMapper.convertToDto(updated);
    }

    public GroupDto deleteTask(Long groupId, Long taskId) {
        Group group = groupRepo.findById(groupId).orElseThrow(() ->
                new ChangesNotAppliedException(String.format("Group id: %d is not found", groupId)));
        Task task = taskRepo.findById(taskId).orElseThrow(() ->
                new ChangesNotAppliedException(String.format("Task id: %d is not found", taskId)));

        group.getTasks().remove(task);
        Group updated = groupRepo.save(group);

        return groupMapper.convertToDto(updated);
    }

    @Transactional
    public GroupDto addGroup(Long groupId, Long groupIdForAdd) {
        Group group = groupRepo.findById(groupId).orElseThrow(() ->
                new ChangesNotAppliedException(String.format("id: %d is not found", groupId)));
        Group children = groupRepo.findById(groupIdForAdd).orElseThrow(() ->
                new ChangesNotAppliedException(String.format("id: %d is not found", groupId)));

        if (Objects.equals(group.getId(), children.getId())) {
            throw new ChangesNotAppliedException("group cannot refer to itself");
        }
//        if (checkCyclicDependency(group.getId(), groupForAdd.getId())) {
//            throw new ChangesNotAppliedException("cyclic dependency");
//      }

        group.getChildren().add(children);
//        children.setParent(group);

        return groupMapper.convertToDto(group);
    }

    @Transactional
    public GroupDto deleteGroup(Long groupId, Long groupIdForDelete) {
        Group group = groupRepo.findById(groupId).orElseThrow(() ->
                new ChangesNotAppliedException(String.format("id: %d is not found", groupId)));
        Group children = groupRepo.findById(groupIdForDelete).orElseThrow(() ->
                new ChangesNotAppliedException(String.format("id: %d is not found", groupId)));

        group.getChildren().remove(children);
//        children.setParent(null);

        return groupMapper.convertToDto(group);
    }

    private boolean checkCyclicDependency(Long id, Group groupId) {
        List<Group> groupList = null /*groupRepo.findByAssignments(groupId)*/;
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
//        cacheService.remove(group.getId(), Group.class);
    }

//    private List<AssignmentImpl> getComposite(Long id) {
//        List<Task> taskList = taskRepo.findByGroupsId(id);
//        List<AssignmentImpl> assignments = taskList.stream().map(task -> {
//            task.setGroups(null);
//            task.setPersons(null);
//            return taskDtoMapper.convertToDto(task);
//        }).collect(Collectors.toList());
//
//        List<Group> groupList = groupRepo.findByAssignmentsId(id);
//        List<Group> groupList = null;
//        List<GroupDto> groupDtos = groupList.stream().map(groupDtoMapper::convertToDto).collect(Collectors.toList());
//        groupDtos.forEach(group -> {
//            group.setPersons(null);
//            group.setAssignments(null);
//            group.setAssignments(getComposite(group.getId()));
//        });
//
//        assignments.addAll(groupDtos);
//
//        return assignments;
//    }
}
