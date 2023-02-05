package com.fastcampus.snsproject.model;

import com.fastcampus.snsproject.model.entity.UserEntity;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.sql.Timestamp;

// TODO : implement
@Getter
@AllArgsConstructor
public class User {

    private Integer id;
    private String userName;
    private String password;
    private UserRole userRole;
    private Timestamp resteredAt;
    private Timestamp updateAt;
    private Timestamp deletedAt;

    // Entity를 DTO로 변환해주는 메소드
    public static User fromEntity(UserEntity entity){
        return new User(
                entity.getId(),
                entity.getUserName(),
                entity.getPassword(),
                entity.getRole(),
                entity.getRegisteredAt(),
                entity.getUpdateAt(),
                entity.getDeletedAt()
        );
    }
}
