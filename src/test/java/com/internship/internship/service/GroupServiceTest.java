package com.internship.internship.service;

import com.internship.internship.dto.GroupDto;
import com.internship.internship.mapper.GroupDtoMapper;
import com.internship.internship.model.Group;
import com.internship.internship.model.Task;
import com.internship.internship.repository.GroupRepo;
import com.internship.internship.repository.TaskRepo;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;

import java.util.ArrayList;
import java.util.List;

import static com.internship.internship.util.Helper.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class GroupServiceTest {

    @InjectMocks
    private GroupService groupService;
    @Mock
    private GroupRepo groupRepo;
    @Mock
    private TaskRepo taskRepo;
    @Mock
    private GroupDtoMapper mapper;

    @Test
    void getById() {
        Group group = new Group(1L);
        GroupDto groupDto = new GroupDto(1L);

        when(groupRepo.getGroupById(groupDto.getId())).thenReturn(group);
        when(mapper.convertToDto(group)).thenReturn(groupDto);

        GroupDto groupFromService = groupService.getById(groupDto.getId());
        assertEquals(groupFromService, groupDto);
        verify(groupRepo, times(1)).getGroupById(group.getId());
    }

    @Test
    void getAll() {
        List<Group> list = new ArrayList<Group>();
        list.add(new Group(33L));
        list.add(new Group(44L));
        list.add(new Group(55L));

        when(groupRepo.getAll()).thenReturn(list);

        List<GroupDto> groupList = groupService.getAll();
        assertEquals(3, groupList.size());
        verify(groupRepo, times(1)).getAll();
    }

    @Test
    void add() {
        GroupDto groupDto = new GroupDto(1L);
        KeyHolder keyHolder = new GeneratedKeyHolder();

        when(groupRepo.addGroup(any(MapSqlParameterSource.class))).thenReturn(keyHolder);
        when(mapper.getDtoFromHolder(any(KeyHolder.class))).thenReturn(groupDto);

        GroupDto groupDtoReturned = groupService.add(groupDto);
        assertEquals(groupDto.getId(), groupDtoReturned.getId());
        verify(groupRepo, times(1)).addGroup(any(MapSqlParameterSource.class));
    }

    @Test
    void update() {
        GroupDto groupDto = newGroupDtoForTest();
        Group group = newGroupForTest();

        when(groupRepo.updateGroup(any(Group.class))).thenReturn(group);
        when(mapper.convertToEntity(groupDto)).thenReturn(group);
        when(mapper.convertToDto(any(Group.class))).thenReturn(groupDto);

        GroupDto dto = groupService.update(groupDto);
        assertEquals(groupDto.getId(), dto.getId());
        verify(groupRepo, times(1)).updateGroup(any(Group.class));
    }

    @Test
    void delete() {
        Group group = newGroupForTest();

        when(groupRepo.deleteGroup(group.getId())).thenReturn(1);

        Integer result = groupService.delete(group.getId());
        assertEquals(1, result);
        verify(groupRepo, times(1)).deleteGroup(group.getId());
    }

    @Test
    void addTask() {
        Group group = new Group(1L);
        Task task = new Task(1L);
        GroupDto groupDto = newGroupDtoForTest();

        when(taskRepo.getTaskById(task.getId())).thenReturn(task);
        when(groupRepo.getGroupById(group.getId())).thenReturn(group);
        when(groupRepo.addTaskToGroup(group.getId(), task.getId())).thenReturn(group);
        when(mapper.convertToDto(group)).thenReturn(groupDto);

        GroupDto result = groupService.addTask(group.getId(), task.getId());
        assertEquals(groupDto, result);
        verify(groupRepo, times(1)).addTaskToGroup(anyLong(), anyLong());
    }

    @Test
    void deleteTask() {
        Group group = newGroupForTest();
        Task task = newTaskForTest();

        when(groupRepo.deleteTaskFromGroup(group.getId(), task.getId())).thenReturn(1);
        groupService.deleteTask(group.getId(), task.getId());
        verify(groupRepo, times(1)).deleteTaskFromGroup(group.getId(), task.getId());
    }

    @Test
    void addGroup() {
        Group group = new Group(2L);
        Group groupIn = new Group(1L);

        when(groupRepo.getGroupById(anyLong())).thenReturn(group);
        when(groupRepo.addGroupToGroup(group.getId(), groupIn.getId())).thenReturn(group);
        when(mapper.convertToDto(any(Group.class))).thenReturn(any(GroupDto.class));

        groupService.addGroup(group.getId(), groupIn.getId());
        verify(groupRepo, times(1)).addGroupToGroup(group.getId(), groupIn.getId());
    }

    @Test
    void deleteGroup() {
        Group group = new Group(2L);
        Group groupIn = new Group(1L);

        when(groupRepo.deleteGroupFromGroup(group.getId(), groupIn.getId())).thenReturn(1);
        groupService.deleteGroup(group.getId(), groupIn.getId());
        verify(groupRepo, times(1)).deleteGroupFromGroup(group.getId(), groupIn.getId());
    }
}