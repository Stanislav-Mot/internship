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
        Group group = new Group();
        GroupDto groupDto = new GroupDto(1L);

        when(groupRepo.getById(groupDto.getId())).thenReturn(group);
        when(mapper.convertToDto(group)).thenReturn(groupDto);

        GroupDto groupFromService = groupService.getById(groupDto.getId());
        assertEquals(groupFromService, groupDto);
        verify(groupRepo, times(1)).getById(group.getId());
    }

    @Test
    void getAll() {
        List<Group> list = new ArrayList<Group>();
        list.add(new Group());
        list.add(new Group());
        list.add(new Group());

        when(groupRepo.findAll()).thenReturn(list);

        List<GroupDto> groupList = groupService.getAll();
        assertEquals(3, groupList.size());
        verify(groupRepo, times(1)).findAll();
    }

    @Test
    void add() {
        GroupDto groupDto = new GroupDto(1L);
        Group group = new Group();

        when(groupRepo.save(any(Group.class))).thenReturn(group);
        when(mapper.getDtoFromHolder(any(KeyHolder.class))).thenReturn(groupDto);

        GroupDto groupDtoReturned = groupService.add(groupDto);
        assertEquals(groupDto.getId(), groupDtoReturned.getId());
        verify(groupRepo, times(1)).save(group);
    }

    @Test
    void update() {
        GroupDto groupDto = newGroupDtoForTest();
        Group group = newGroupForTest();

        when(groupRepo.save(any(Group.class))).thenReturn(group);
        when(mapper.convertToEntity(groupDto)).thenReturn(group);
        when(mapper.convertToDto(any(Group.class))).thenReturn(groupDto);

        GroupDto dto = groupService.update(groupDto);
        assertEquals(groupDto.getId(), dto.getId());
        verify(groupRepo, times(1)).save(any(Group.class));
    }

    @Test
    void addTask() {
        Group group = new Group();
        Task task = new Task();
        GroupDto groupDto = newGroupDtoForTest();

        when(taskRepo.getById(task.getId())).thenReturn(task);
        when(groupRepo.getById(group.getId())).thenReturn(group);
//        when(groupRepo.addTaskToGroup(group.getId(), task.getId())).thenReturn(void);
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
        Group group = new Group();
        Group groupIn = new Group();

        when(groupRepo.getById(anyLong())).thenReturn(group);
        when(groupRepo.addGroupToGroup(group.getId(), groupIn.getId())).thenReturn(1);
        when(mapper.convertToDto(any(Group.class))).thenReturn(any(GroupDto.class));

        groupService.addGroup(group.getId(), groupIn.getId());
        verify(groupRepo, times(1)).addGroupToGroup(group.getId(), groupIn.getId());
    }

    @Test
    void deleteGroup() {
        Group group = new Group();
        Group groupIn = new Group();

        when(groupRepo.deleteGroupFromGroup(group.getId(), groupIn.getId())).thenReturn(1);
        groupService.deleteGroup(group.getId(), groupIn.getId());
        verify(groupRepo, times(1)).deleteGroupFromGroup(group.getId(), groupIn.getId());
    }
}