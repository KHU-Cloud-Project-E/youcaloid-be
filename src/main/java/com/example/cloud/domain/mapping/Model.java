package com.example.cloud.domain.mapping;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@NoArgsConstructor
@AllArgsConstructor
@Entity(name = "MODEL_INFO")
public class Model {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "MODELID")
    private Long id;

    @Column(name = "MODELPATH", length = 300)
    private String path;

    @Column(name = "DESCRIPTION", length = 30)
    private String description;



}