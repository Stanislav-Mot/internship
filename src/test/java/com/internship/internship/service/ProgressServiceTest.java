package com.internship.internship.service;

import com.internship.internship.dto.ProgressDto;
import com.internship.internship.mapper.ProgressDtoMapper;
import com.internship.internship.model.Progress;
import com.internship.internship.repository.ProgressRepo;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;

import java.util.ArrayList;
import java.util.List;

import static com.internship.internship.util.Helper.newProgressDtoForTest;
import static com.internship.internship.util.Helper.newProgressForTest;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ProgressServiceTest {

    private final Long CORRECT_ID = 999L;
    @InjectMocks
    private ProgressService progressService;
    @Mock
    private ProgressRepo progressRepo;

    @Test
    void getById() {
        Progress progress = newProgressForTest();

        when(progressRepo.getProgressById(progress.getId())).thenReturn(progress);

        ProgressDto progressFromService = progressService.getById(progress.getId());

        assertEquals(progressFromService, progress);

        verify(progressRepo, times(1)).getProgressById(progress.getId());
    }

    @Test
    void getAll() {
        List<Progress> list = new ArrayList<Progress>();
        list.add(new Progress(33L));
        list.add(new Progress(44L));
        list.add(new Progress(55L));

        when(progressRepo.getAllProgresses()).thenReturn(list);

        List<ProgressDto> progressList = progressService.getAll();

        assertEquals(3, progressList.size());
        verify(progressRepo, times(1)).getAllProgresses();
    }

    @Test
    void add() {
        ProgressDto progressDto = newProgressDtoForTest();

        when(progressRepo.addProgress(any(MapSqlParameterSource.class))).thenReturn(1);

        Integer result = progressService.add(progressDto);

        assertEquals(1, result);

        verify(progressRepo, times(1)).addProgress(any(MapSqlParameterSource.class));
    }

    @Test
    void update() {
        ProgressDto progressDto = newProgressDtoForTest();

        when(progressRepo.updateProgresses(any(MapSqlParameterSource.class))).thenReturn(1);

        Integer result = progressService.update(progressDto);

        assertEquals(1, result);

        verify(progressRepo, times(1)).updateProgresses(any(MapSqlParameterSource.class));
    }

    @Test
    void delete() {
        Progress progress = newProgressForTest();

        when(progressRepo.deleteProgress(progress.getId())).thenReturn(1);

        Integer result = progressService.delete(progress.getId());

        assertEquals(1, result);

        verify(progressRepo, times(1)).deleteProgress(progress.getId());
    }
}