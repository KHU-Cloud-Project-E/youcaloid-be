package com.example.cloud.domain.mapping;


import com.example.cloud.domain.common.BaseEntity;
import lombok.*;
import org.hibernate.annotations.DynamicInsert;
import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Builder
@Getter
@DynamicInsert
@ToString
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor(access = AccessLevel.PUBLIC)
public class User extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_idx")
    private Long id;

    @Column(length = 30)
    private String nickname;

    private String refresh_token;

    @Column(name = "register_id")
    private String register_id;

    private String email;


    private Integer platform;


    private Integer status;

    @Enumerated(EnumType.STRING)
    private Role role;


}
