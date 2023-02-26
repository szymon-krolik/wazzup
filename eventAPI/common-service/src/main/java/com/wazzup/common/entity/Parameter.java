package com.wazzup.common.entity;

import lombok.Data;

import javax.persistence.*;

@Entity
@Data
@Table(name = "PARAMETER")
public class Parameter {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "PARAMETER_ID")
    private Long id;

    @Column(name = "CODE")
    private String code;

    @Column(name = "VALUE")
    private int value;

    @Column(name = "DESCRIPTION")
    private String description;
}
