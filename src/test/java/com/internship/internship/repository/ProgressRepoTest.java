package com.internship.internship.repository;

import com.internship.internship.exeption.DataNotFoundException;
import com.internship.internship.model.Progress;
import com.internship.internship.model.Task;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.List;

import static com.internship.internship.service.ProgressService.getMapSqlParameterSource;
import static org.assertj.core.api.Assertions.from;
import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@ActiveProfiles("test")
@TestPropertySource("/application-test.properties")
@Sql("/schema-for-test.sql")
@Sql("/data-for-progress-test.sql")
class ProgressRepoTest {

    private final Long CORRECT_ID = 999L;
    private final Long ID_FOR_GET = 11L;
    private final Long ID_FOR_UPDATE = 12L;
    private final Long ID_FOR_DELETE = 13L;
    @Autowired
    private ProgressRepo progressRepo;
    private Integer countProgresses = 4;

    @Test
    void getProgressById() {
        Progress progress = progressRepo.getProgressById(ID_FOR_GET);

        Assertions.assertThat(progress).returns(ID_FOR_GET.shortValue(), from(Progress::getPercents));
    }

    @Test
    void getAllProgresses() {
        List<Progress> progresses = progressRepo.getAllProgresses();

        assertEquals(countProgresses, progresses.size());
    }

    @Test
    void addProgress() {
        MapSqlParameterSource parameters = getMapSqlParameterSource(new Progress(CORRECT_ID, new Task(8877L), (short) 99));

        progressRepo.addProgress(parameters);
        Iterable<Progress> progresses = progressRepo.getAllProgresses();

        Assertions.assertThat(progresses).extracting(Progress::getPercents).contains((short) 99);

        countProgresses += 1;
    }

    @Test
    void updateProgresses() {
        MapSqlParameterSource parameters = getMapSqlParameterSource(new Progress(ID_FOR_UPDATE, new Task(9999L), (short) 34));

        Integer answer = progressRepo.updateProgresses(parameters);

        assertEquals(1, answer);

        Progress progress = progressRepo.getProgressById(ID_FOR_UPDATE);

        Assertions.assertThat(progress).returns((short) 34, from(Progress::getPercents));

    }

    @Test
    void deleteProgress() {
        Integer answer = progressRepo.deleteProgress(ID_FOR_DELETE);

        assertEquals(1, answer);

        Assertions.assertThatThrownBy(() -> progressRepo.getProgressById(ID_FOR_DELETE)).isInstanceOf(DataNotFoundException.class);
    }
}