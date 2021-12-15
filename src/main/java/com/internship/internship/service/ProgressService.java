package com.internship.internship.service;

import com.internship.internship.model.Progress;
import com.internship.internship.repository.ProgressRepo;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProgressService {

    private final ProgressRepo progressRepo;

    public ProgressService(ProgressRepo progressRepo) {
        this.progressRepo = progressRepo;
    }

    public Progress getById(Long id) {
        return progressRepo.getProgressById(id);
    }

    public List<Progress> getAll() {
        return progressRepo.getAllProgresses();
    }

    public Integer add(Progress progress) {
        MapSqlParameterSource parameters = getMapSqlParameterSource(progress);

        return progressRepo.addProgress(parameters);
    }


    public Integer update(Progress progress) {
        MapSqlParameterSource parameters = getMapSqlParameterSource(progress);

        return progressRepo.updateProgresses(parameters);
    }

    public Integer delete(Long id) {
        return progressRepo.deleteProgress(id);
    }

    private MapSqlParameterSource getMapSqlParameterSource(Progress progress) {
        Long taskID = (progress.getTask() != null) ? progress.getTask().getId() : null;

        MapSqlParameterSource parameters = new MapSqlParameterSource();
        parameters.addValue("id", progress.getId());
        parameters.addValue("taskId", taskID);
        parameters.addValue("percents", progress.getPercents());
        return parameters;
    }
}
