package com.saud.blog.Controllers;

import com.saud.blog.Mappers.PostMapper;
import com.saud.blog.Services.PostService;
import com.saud.blog.Services.UserService;
import com.saud.blog.domain.CreatePostRequest;
import com.saud.blog.domain.DTOs.CreatePostRequestDto;
import com.saud.blog.domain.DTOs.PostDto;
import com.saud.blog.domain.DTOs.UpdatePostRequestDto;
import com.saud.blog.domain.UpdatePostRequest;
import com.saud.blog.domain.entities.Post;
import com.saud.blog.domain.entities.User;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.graphql.GraphQlProperties;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping(path = "/api/v1/posts")
@RequiredArgsConstructor
public class PostController {
    private final PostService postService;
    private final PostMapper postMapper;

    private final UserService userService;

    @GetMapping
    public ResponseEntity<List<PostDto>> getAllPosts(@RequestParam(required = false)UUID categoryId, @RequestParam(required = false) UUID tagId) {

        List<Post> posts = postService.getAllPosts(categoryId,tagId);
        List<PostDto> postDtos = posts.stream().map(postMapper::toDto).toList();

        return ResponseEntity.ok(postDtos);
    }

    @GetMapping(path = "/drafts")
    public ResponseEntity<List<PostDto>> getDrafts(@RequestAttribute UUID userId) {
        User loggedInUser = userService.getUserById(userId);
        List<Post> draftPosts = postService.getDraftPosts(loggedInUser);

        List<PostDto> draftDtos = draftPosts.stream().map(postMapper::toDto).toList();

        return ResponseEntity.ok(draftDtos);

    }

    @PostMapping
    public ResponseEntity<PostDto> createPost(
            @Valid @RequestBody CreatePostRequestDto createPostRequestDto,
            @RequestAttribute UUID userId
    ) {
        User loggedInUser = userService.getUserById(userId);
        CreatePostRequest createPostRequest = postMapper.toCreatePostRequest(createPostRequestDto);
        Post CreatedPost = postService.createPost(loggedInUser,createPostRequest);
        PostDto createdPostDto =  postMapper.toDto(CreatedPost);

        return new ResponseEntity<>(createdPostDto, HttpStatus.CREATED);
    }

    @PutMapping(path = "/{id}")
    public ResponseEntity<PostDto> updatePost(
            @PathVariable UUID id, @Valid @RequestBody UpdatePostRequestDto updatePostRequestDto, @RequestAttribute UUID userId) {
        UpdatePostRequest updatePostRequest = postMapper.toUpdatePostRequest(updatePostRequestDto);

        Post updatedPost = postService.updatePost(id,updatePostRequest, userId);
        PostDto updatePostDto = postMapper.toDto(updatedPost);
        return ResponseEntity.ok(updatePostDto);
    }

    @GetMapping(path = "/{id}")
    public ResponseEntity<PostDto> getPost(@PathVariable UUID id) {
        Post post = postService.getPost(id);
        PostDto postDto = postMapper.toDto(post);

        return ResponseEntity.ok(postDto);
    }

    @DeleteMapping(path = "/{id}")
    public ResponseEntity<Void> deletePost(@PathVariable UUID id, @RequestAttribute UUID userId) {
        postService.deletePost(id, userId);

        return ResponseEntity.noContent().build();
    }

}
