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
    private String ToStartTime;
    private Short fromProgress;
    private Short toProgress;
}
