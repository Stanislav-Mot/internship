package com.internship.internship.service;

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

import static com.internship.internship.util.Helper.newGroupForTest;
import static com.internship.internship.util.Helper.newTaskForTest;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class GroupServiceTest {

    private final Long CORRECT_ID = 999L;
    @InjectMocks
    private GroupService groupService;
    @Mock
    private GroupRepo groupRepo;

    @Test
    void getById() {
        Group group = newGroupForTest();

        when(groupRepo.getGroupById(group.getId())).thenReturn(group);

        Group groupFromService = groupService.getById(group.getId());

        assertEquals(groupFromService, group);

        verify(groupRepo, times(1)).getGroupById(group.getId());
    }

    @Test
    void getAll() {
        List<Group> list = new ArrayList<Group>();
        list.add(new Group(33L));
        list.add(new Group(44L));
        list.add(new Group(55L));

        when(groupRepo.getAll()).thenReturn(list);

        List<Group> groupList = groupService.getAll();

        assertEquals(3, groupList.size());
        verify(groupRepo, times(1)).getAll();
    }

    @Test
    void add() {
        Group group = newGroupForTest();

        when(groupRepo.addGroup(any(MapSqlParameterSource.class))).thenReturn(1);

        Integer result = groupService.add(group);

        assertEquals(1, result);

        verify(groupRepo, times(1)).addGroup(any(MapSqlParameterSource.class));
    }

    @Test
    void update() {
        Group group = newGroupForTest();

        when(groupRepo.updateGroup(any(Group.class))).thenReturn(1);

        Integer result = groupService.update(group);

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

        when(groupRepo.addTaskToGroup(group.getId(), task)).thenReturn(1);

        Integer result = groupService.addTask(group.getId(), task);

        assertEquals(1, result);

        verify(groupRepo, times(1)).addTaskToGroup(group.getId(), task);
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
}