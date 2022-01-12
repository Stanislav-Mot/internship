package com.internship.internship.model.Composite;

import io.swagger.v3.oas.annotations.Hidden;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Hidden
@NoArgsConstructor
@AllArgsConstructor
@Data
public class CompositeTask implements ParentTask {
    private List<ParentTask> childParentTask = new ArrayList<>();

    public void add(ParentTask task) {
        childParentTask.add(task);
    }

    public void addAll(List<? extends ParentTask> taskList) {
        childParentTask.addAll(taskList);
    }

    public Integer size() {
        return childParentTask.size();
    }
}