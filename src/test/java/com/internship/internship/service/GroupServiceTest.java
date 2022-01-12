package com.internship.internship.service;

import com.internship.internship.dto.GroupDto;
import com.internship.internship.mapper.GroupDtoMapper;
import com.internship.internship.mapper.TaskDtoMapper;
import com.internship.internship.model.Group;
import com.internship.internship.model.Task;
import com.internship.internship.repository.GroupRepo;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;

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
    private GroupDtoMapper mapper;
    @Mock
    private TaskDtoMapper taskDtoMapper;

    @Test
    void getById() {
        Group group = newGroupForTest();
        GroupDto groupDto = newGroupDtoForTest();

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
        GroupDto groupDto = newGroupDtoForTest();

        when(groupRepo.addGroup(any(MapSqlParameterSource.class))).thenReturn(1);

        Integer result = groupService.add(groupDto);

        assertEquals(1, result);

        verify(groupRepo, times(1)).addGroup(any(MapSqlParameterSource.class));
    }

    @Test
    void update() {
        GroupDto groupDto = newGroupDtoForTest();
        Group group = newGroupForTest();

        when(groupRepo.updateGroup(any(Group.class))).thenReturn(1);
        when(mapper.convertToEntity(groupDto)).thenReturn(group);

        Integer result = groupService.update(groupDto);

        assertEquals(1, result);

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
        Group group = newGroupForTest();
        Task task = newTaskForTest();

        when(groupRepo.addTaskToGroup(group.getId(), task.getId())).thenReturn(1);

        Integer result = groupService.addTask(group.getId(), task.getId());

        assertEquals(1, result);

        verify(groupRepo, times(1)).addTaskToGroup(group.getId(), task.getId());
    }

    @Test
    void deleteTask() {
        Group group = newGroupForTest();
        Task task = newTaskForTest();

        when(groupRepo.deleteTaskFromGroup(group.getId(), task.getId())).thenReturn(1);

        Integer result = groupService.deleteTask(group.getId(), task.getId());

        assertEquals(1, result);

        verify(groupRepo, times(1)).deleteTaskFromGroup(group.getId(), task.getId());
    }

    @Test
    void addGroup() {
        Group group = new Group(2L);
        Group groupIn = new Group(1L);

        when(groupRepo.addGroupToGroup(group.getId(), groupIn.getId())).thenReturn(1);

        Integer result = groupService.addGroup(group.getId(), groupIn.getId());

        assertEquals(1, result);

        verify(groupRepo, times(1)).addGroupToGroup(group.getId(), groupIn.getId());
    }

    @Test
    void deleteGroup() {
        Group group = new Group(2L);
        Group groupIn = new Group(1L);

        when(groupRepo.deleteGroupFromGroup(group.getId(), groupIn.getId())).thenReturn(1);

        Integer result = groupService.deleteGroup(group.getId(), groupIn.getId());

        assertEquals(1, result);

        verify(groupRepo, times(1)).deleteGroupFromGroup(group.getId(), groupIn.getId());
    }
}