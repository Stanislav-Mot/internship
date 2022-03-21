package com.internship.internship.model;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@NoArgsConstructor
@Data
@Entity
@Table(name = "assignment")
public class Composite {

    @Id
    private Long id;

    private Long group_id;

    private Long children_id;

    private Long person_id;

    private Long task_id;
}