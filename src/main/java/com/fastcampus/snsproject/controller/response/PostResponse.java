package com.fastcampus.snsproject.controller.response;

import com.fastcampus.snsproject.model.Post;
import com.fastcampus.snsproject.model.User;
import com.fastcampus.snsproject.model.entity.PostEntity;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.sql.Timestamp;

@Getter
@AllArgsConstructor
public class PostResponse {

    private Integer id;
    private String title;
    private String body;
    private UserResponse user;
    private Timestamp registeredAt;
    private Timestamp updateAt;
    private Timestamp deletedAt;

    public static PostResponse fromPost(Post post){
        return new PostResponse(
                post.getId(),
                post.getTitle(),
                post.getBody(),
                UserResponse.fromUser(post.getUser()),
                post.getRegisteredAt(),
                post.getUpdateAt(),
                post.getDeletedAt()
        );
    }

}