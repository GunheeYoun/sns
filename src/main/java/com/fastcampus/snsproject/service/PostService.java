package com.fastcampus.snsproject.service;

import com.fastcampus.snsproject.controller.response.Response;
import com.fastcampus.snsproject.exception.ErrorCode;
import com.fastcampus.snsproject.exception.SnsApplicationException;
import com.fastcampus.snsproject.model.Comment;
import com.fastcampus.snsproject.model.Post;
import com.fastcampus.snsproject.model.entity.CommentEntity;
import com.fastcampus.snsproject.model.entity.LikeEntity;
import com.fastcampus.snsproject.model.entity.PostEntity;
import com.fastcampus.snsproject.model.entity.UserEntity;
import com.fastcampus.snsproject.repository.CommentEntityRepository;
import com.fastcampus.snsproject.repository.LikeEntityRepository;
import com.fastcampus.snsproject.repository.PostEntityRepository;
import com.fastcampus.snsproject.repository.UserEntityRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;


@Service
@RequiredArgsConstructor
public class PostService {

    private final PostEntityRepository postEntityRepository;
    private final UserEntityRepository userEntityRepository;
    private final LikeEntityRepository likeEntityRepository;
    private final CommentEntityRepository commentEntityRepository;

    @Transactional
    public void create(String title, String body, String userName){
        // user find
        UserEntity userEntity = getUserEntityOrException(userName);
        // post save
        postEntityRepository.save(PostEntity.of(title,body,userEntity));

    }

    @Transactional
    public Post modify(String title, String body, String userName, Integer postId) {
        // user find
        UserEntity userEntity = getUserEntityOrException(userName);
        // post exist
        PostEntity postEntity = getPostEntityOrException(postId);

        // post permission
        if (postEntity.getUser() != userEntity ) {
            throw new SnsApplicationException(ErrorCode.INVALID_PERMISSION
                    , String.format("%s has no permission with %s",userName, postId));
        }

        postEntity.setTitle(title);
        postEntity.setBody(body);

        // save
        return Post.fromEntity(postEntityRepository.saveAndFlush(postEntity));
    }

    @Transactional
    public void delete(String userName, Integer postId) {
        // user find
        UserEntity userEntity = getUserEntityOrException(userName);
        // post exist
        PostEntity postEntity = getPostEntityOrException(postId);


        // post permission
        if (postEntity.getUser() != userEntity ) {
            throw new SnsApplicationException(ErrorCode.INVALID_PERMISSION
                    , String.format("%s has no permission with %s",userName, postId));
        }

        //delete
        postEntityRepository.delete(postEntity);


    }

    public Page<Post> list(Pageable pageable) {
        return postEntityRepository.findAll(pageable).map(Post::fromEntity);
    }

    public Page<Post> my(String userName, Pageable pageable) {
        // user find
        UserEntity userEntity = getUserEntityOrException(userName);

        return postEntityRepository.findAllByUser(userEntity, pageable).map(Post::fromEntity);
    }
//    public Page<Post> my(Integer userId, Pageable pageable) {
//        return postEntityRepository.findAllByUserId(userId, pageable).map(Post::fromEntity);
//    }

    @Transactional
    public void like(Integer postId, String userName) {
        // user find
        UserEntity userEntity = getUserEntityOrException(userName);
        // post exist
        PostEntity postEntity = getPostEntityOrException(postId);


        // check liked -> throw
        likeEntityRepository.findByUserAndPost(userEntity,postEntity)
                .ifPresent( it -> {
                    throw new SnsApplicationException(ErrorCode.ALREADY_LIKED, String.format("userName %s already like postId %d",userName, postId));
                });

        // like save
        likeEntityRepository.save(LikeEntity.of(userEntity,postEntity));
    }

    @Transactional
    public int likeCount(Integer postId) {
        // post exist
        PostEntity postEntity = getPostEntityOrException(postId);

        // count like
//        List<LikeEntity> likeEntities = likeEntityRepository.findAllByPost(postEntity);
//        return likeEntities.size();
        return likeEntityRepository.countByPost(postEntity);
    }

    @Transactional
    public void comment(Integer postId, String userName, String comment) {
        // user find
        UserEntity userEntity = getUserEntityOrException(userName);
        // post exist
        PostEntity postEntity = getPostEntityOrException(postId);

        commentEntityRepository.save(CommentEntity.of(userEntity,postEntity,comment));
    }

    public Page<Comment> getComments(Integer postId, Pageable pageable) {
        // post exist
        PostEntity postEntity = getPostEntityOrException(postId);

        return commentEntityRepository.findAllByPost(postEntity, pageable).map(Comment::fromEntity);
    }

    // post exist
    private PostEntity getPostEntityOrException(Integer postId) {
        return postEntityRepository.findById(postId).orElseThrow(() ->
                new SnsApplicationException(ErrorCode.POST_NOT_FOUND, String.format("%s not founded", postId)));
    }

    // user exist
    private UserEntity getUserEntityOrException(String userName) {
        return userEntityRepository.findByUserName(userName).orElseThrow(() ->
                new SnsApplicationException(ErrorCode.USER_NOT_FOUND, String.format("%s not founded", userName)));
    }
}
