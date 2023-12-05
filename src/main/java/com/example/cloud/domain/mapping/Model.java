package com.example.cloud.domain.mapping;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Builder
@Getter
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

    @Column(name = "NAME", length = 30)
    private String name;

    @Column(name = "DETAIL", length = 500)
    private String description;

    private int share_check;

    private String image_url;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;




}