package com.internship.internship.model.search;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class SearchTask {
    private Long id;
    private String name;
    private String fromStartTime;
    private String ToStartTime; // формат имени переменной. также - в каком формате пользователь должен воодить дату? нигде не отражено и не проверяется
    private Short fromProgress;
    private Short toProgress;

    public SearchTask(Long id, String name, String fromStartTime, String toStartTime) {
        this.id = id;
        this.name = name;
        this.fromStartTime = fromStartTime;
        ToStartTime = toStartTime;
    }
}
