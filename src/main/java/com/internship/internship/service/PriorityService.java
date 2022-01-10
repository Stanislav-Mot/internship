package com.internship.internship.service;

import com.internship.internship.dto.PriorityDto;
import com.internship.internship.mapper.PriorityDtoMapper;
import com.internship.internship.model.Priority;
import com.internship.internship.repository.PriorityRepo;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class PriorityService {

    private final PriorityRepo priorityRepo;
    private final PriorityDtoMapper mapper;

    public PriorityService(PriorityRepo priorityRepo, PriorityDtoMapper priorityDtoMapper) {
        this.priorityRepo = priorityRepo;
        this.mapper = priorityDtoMapper;
    }

    private static MapSqlParameterSource getPriorityMapSqlPS(Priority priority) {
        MapSqlParameterSource parameters = new MapSqlParameterSource();
        parameters.addValue("id_group", priority.getId());
        parameters.addValue("id_task", priority.getTaskId());
        parameters.addValue("priority", priority.getPriority());
        return parameters;
    }

    public Integer delete(Long id) {
        return priorityRepo.deletePriority(id);
    }

    public Integer update(PriorityDto priorityDto) {
        Priority priority = mapper.convertToEntity(priorityDto);
        MapSqlParameterSource parameters = getPriorityMapSqlPS(priority);

        return priorityRepo.updatePriority(parameters);
    }


    public Integer add(PriorityDto priorityDto) {
        Priority priority = mapper.convertToEntity(priorityDto);
        MapSqlParameterSource parameters = getPriorityMapSqlPS(priority);

        return priorityRepo.addPriority(parameters);
    }

    public PriorityDto getById(Long id) {
        Priority priority = priorityRepo.getPriorityById(id);
        PriorityDto priorityDto = mapper.convertToDto(priority);
        return priorityDto;
    }

    public List<PriorityDto> getByGroupId(Long id) {
        List<Priority> progresses = priorityRepo.getAllPriorityByGroupId(id);
        return getPriorityDtos(progresses);
    }

    public List<PriorityDto> getAll() {
        List<Priority> progresses = priorityRepo.getAllPriority();
        return getPriorityDtos(progresses);
    }


    private List<PriorityDto> getPriorityDtos(List<Priority> progresses) {
        if (progresses != null) {
            List<PriorityDto> priorityDtos = new ArrayList<>();
            for (Priority priority : progresses)
                priorityDtos.add(mapper.convertToDto(priority));
            return priorityDtos;
        } else {
            return null;
        }
    }
}
