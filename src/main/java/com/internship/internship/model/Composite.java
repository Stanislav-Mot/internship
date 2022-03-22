package com.internship.internship.model;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
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

    @Column(name = "group_id")
    private Long groupId;

    @Column(name = "children_id")
    private Long childrenId;

    @Column(name = "person_id")
    private Long personId;

    @Column(name = "task_id")
    private Long taskId;
}