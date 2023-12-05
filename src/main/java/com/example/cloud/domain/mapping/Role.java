package com.example.cloud.domain.mapping;

import lombok.Getter;

@Getter
public enum Role {
    ROLE_USER("사용자");
    private String description;

    Role(String description){
        this.description =description;
    }
}

