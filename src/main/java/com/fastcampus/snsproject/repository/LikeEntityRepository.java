package com.fastcampus.snsproject.repository;

import com.fastcampus.snsproject.model.entity.LikeEntity;
import com.fastcampus.snsproject.model.entity.PostEntity;
import com.fastcampus.snsproject.model.entity.UserEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;


public interface LikeEntityRepository extends JpaRepository<LikeEntity,Integer> {
    public Optional<LikeEntity> findByUserAndPost(UserEntity user, PostEntity post);

    //select count(*) from "like" where post_id = 49
    @Query(value = "SELECT COUNT(*) FROM LikeEntity entity WHERE entity.post =:post")
    Integer countByPost(@Param("post") PostEntity post);

    public List<LikeEntity> findAllByPost(PostEntity post);
}
