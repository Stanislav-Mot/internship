package com.internship.internship.service;

import com.internship.internship.dto.PriorityDto;
import com.internship.internship.mapper.PriorityDtoMapper;
import com.internship.internship.model.Group;
import com.internship.internship.model.Priority;
import com.internship.internship.model.Task;
import com.internship.internship.repository.PriorityRepo;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;

import java.util.ArrayList;
import java.util.List;

import static com.internship.internship.util.Helper.newPriorityDtoForTest;
import static com.internship.internship.util.Helper.newPriorityForTest;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PriorityServiceTest {

    @InjectMocks
    private PriorityService priorityService;
    @Mock
    private PriorityRepo priorityRepo;
    @Mock
    private PriorityDtoMapper mapper;

    @Test
    void delete() {
        Priority priority = newPriorityForTest();

        when(priorityRepo.deletePriority(priority.getId())).thenReturn(1);

        Integer result = priorityRepo.deletePriority(priority.getId());

        assertEquals(1, result);

        verify(priorityRepo, times(1)).deletePriority(priority.getId());
    }

    @Test
    void update() {
        PriorityDto priorityDto = newPriorityDtoForTest();
        Priority priority = newPriorityForTest();

        when(priorityRepo.updatePriority(any(MapSqlParameterSource.class))).thenReturn(1);
        when(mapper.convertToEntity(priorityDto)).thenReturn(priority);

        Integer result = priorityService.update(priorityDto);

        assertEquals(1, result);

        verify(priorityRepo, times(1)).updatePriority(any(MapSqlParameterSource.class));
    }

    @Test
    void add() {
        PriorityDto priorityDto = newPriorityDtoForTest();
        Priority priority = newPriorityForTest();

        when(priorityRepo.addPriority(any(MapSqlParameterSource.class))).thenReturn(1);
        when(mapper.convertToEntity(priorityDto)).thenReturn(priority);

        Integer result = priorityService.add(priorityDto);

        assertEquals(1, result);

        verify(priorityRepo, times(1)).addPriority(any(MapSqlParameterSource.class));
    }

    @Test
    void getById() {
        PriorityDto priorityDto = newPriorityDtoForTest();
        Priority priority = newPriorityForTest();

        when(priorityRepo.getPriorityById(priority.getId())).thenReturn(priority);
        when(mapper.convertToDto(priority)).thenReturn(priorityDto);

        PriorityDto priorityFromService = priorityService.getById(priority.getId());

        assertEquals(priorityFromService, priorityDto);

        verify(priorityRepo, times(1)).getPriorityById(priority.getId());
    }

    @Test
    void getByGroupId() {
        List<Priority> list = new ArrayList<>();
        list.add(new Priority(1L, new Group(1L), new Task(1L), 99));
        list.add(new Priority(2L, new Group(1L), new Task(1L), 99));
        list.add(new Priority(3L, new Group(1L), new Task(1L), 99));

        when(priorityRepo.getAllPriorityByGroupId(2L)).thenReturn(list);

        List<PriorityDto> priorityDtoList = priorityService.getByGroupId(2L);

        assertEquals(3, priorityDtoList.size());
        verify(priorityRepo, times(1)).getAllPriorityByGroupId(2L);
    }

    @Test
    void getAll() {
        List<Priority> list = new ArrayList<>();
        list.add(new Priority(1L, new Group(1L), new Task(1L), 99));
        list.add(new Priority(2L, new Group(1L), new Task(1L), 99));
        list.add(new Priority(3L, new Group(1L), new Task(1L), 99));

        when(priorityRepo.getAllPriority()).thenReturn(list);

        List<PriorityDto> priorityDtoList = priorityService.getAll();

        assertEquals(3, priorityDtoList.size());
        verify(priorityRepo, times(1)).getAllPriority();
    }
}