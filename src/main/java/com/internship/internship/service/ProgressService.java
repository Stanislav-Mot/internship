package com.internship.internship.service;

import com.internship.internship.dto.ProgressDto;
import com.internship.internship.dto.TaskDto;
import com.internship.internship.mapper.ProgressDtoMapper;
import com.internship.internship.model.Progress;
import com.internship.internship.model.Task;
import com.internship.internship.repository.ProgressRepo;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class ProgressService {

    private final ProgressRepo progressRepo;
    private final ProgressDtoMapper mapper;

    public ProgressService(ProgressRepo progressRepo, ProgressDtoMapper mapper) {
        this.progressRepo = progressRepo;
        this.mapper = mapper;
    }

    public static MapSqlParameterSource getMapSqlParameterSource(Progress progress) {
        Long taskID = (progress.getTask() != null) ? progress.getTask().getId() : null;

        MapSqlParameterSource parameters = new MapSqlParameterSource();
        parameters.addValue("id", progress.getId());
        parameters.addValue("id_task", taskID);
        parameters.addValue("percents", progress.getPercents());
        return parameters;
    }

    public ProgressDto getById(Long id) {
        Progress progress = progressRepo.getProgressById(id);
        ProgressDto progressDto = mapper.convertToDto(progress);
        return progressDto;
    }

    public List<ProgressDto> getAll() {
        List<Progress> progresses = progressRepo.getAllProgresses();
        if(progresses != null){
            List<ProgressDto> progressDtos = new ArrayList<>();
            for (Progress progress : progresses)
                progressDtos.add(mapper.convertToDto(progress));
            return progressDtos;
        }
        return null;
    }

    public Integer add(ProgressDto progressDto) {
        Progress progress = mapper.convertToEntity(progressDto);

        MapSqlParameterSource parameters = getMapSqlParameterSource(progress);

        return progressRepo.addProgress(parameters);
    }

    public Integer update(ProgressDto progressDto) {
        Progress progress = mapper.convertToEntity(progressDto);
        MapSqlParameterSource parameters = getMapSqlParameterSource(progress);

        return progressRepo.updateProgresses(parameters);
    }

    public Integer delete(Long id) {
        return progressRepo.deleteProgress(id);
    }
}
