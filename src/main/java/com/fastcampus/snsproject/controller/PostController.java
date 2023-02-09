package com.fastcampus.snsproject.controller;

import com.fastcampus.snsproject.controller.request.PostCreateRequest;
import com.fastcampus.snsproject.controller.response.PostResponse;
import com.fastcampus.snsproject.controller.response.Response;
import com.fastcampus.snsproject.model.Post;
import com.fastcampus.snsproject.service.PostService;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/posts")
@AllArgsConstructor
public class PostController {

    private final PostService postService;


    @PostMapping
    public Response<Void> create(@RequestBody PostCreateRequest request, Authentication authentication){
        postService.create(request.getTitle(),request.getBody(),authentication.getName());

        return Response.success();
    }

    @PutMapping("/{postId}")
    public Response<PostResponse> modify(@PathVariable Integer postId, @RequestBody PostCreateRequest request, Authentication authentication){
        Post post = postService.modify(request.getTitle(),request.getBody(),authentication.getName(), postId);

        return Response.success(PostResponse.fromPost(post));
    }

}
