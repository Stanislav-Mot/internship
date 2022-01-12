package com.internship.internship.mapper;

import com.internship.internship.dto.ProgressDto;
import com.internship.internship.model.Progress;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static com.internship.internship.util.Helper.newProgressDtoForTest;
import static com.internship.internship.util.Helper.newProgressForTest;
import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(SpringExtension.class)
@SpringBootTest
class ProgressDtoMapperTest {

    @Autowired
    private ProgressDtoMapper progressDtoMapper;

    @Test
    void convertToDto() {
        Progress progress = newProgressForTest();
        ProgressDto progressDto = progressDtoMapper.convertToDto(progress);

        assertEquals(progress.getId(), progressDto.getId());
        assertEquals(progress.getPercents(), progressDto.getPercents());
        assertEquals(progress.getTask().getId(), progressDto.getTask().getId());
    }

    @Test
    void convertToEntity() {
        ProgressDto progressDto = newProgressDtoForTest();
        Progress progress = progressDtoMapper.convertToEntity(progressDto);

        assertEquals(progress.getId(), progressDto.getId());
        assertEquals(progress.getPercents(), progressDto.getPercents());
        assertEquals(progress.getTask().getId(), progressDto.getTask().getId());
    }
}