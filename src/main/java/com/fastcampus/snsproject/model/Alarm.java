package com.fastcampus.snsproject.model;

import com.fastcampus.snsproject.model.entity.AlarmEntity;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.sql.Timestamp;

// TODO : implement
@Getter
@AllArgsConstructor
public class Alarm {

    private Integer id;
//    private User user;
    private AlarmType alarmType;
    private AlarmArgs Args;
    private Timestamp registeredAt;
    private Timestamp updatedAt;
    private Timestamp deletedAt;

    // Entity를 DTO로 변환해주는 메소드
    public static Alarm fromEntity(AlarmEntity entity){
        return new Alarm(
                entity.getId(),
//                User.fromEntity(entity.getUser()),
                entity.getAlarmType(),
                entity.getArgs(),
                entity.getRegisteredAt(),
                entity.getUpdatedAt(),
                entity.getDeletedAt()
        );
    }
}
