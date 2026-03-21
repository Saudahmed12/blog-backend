package com.saud.blog.Services;

import com.saud.blog.domain.CreatePostRequest;
import com.saud.blog.domain.UpdatePostRequest;
import com.saud.blog.domain.entities.Post;
import com.saud.blog.domain.entities.User;

import java.util.List;
import java.util.UUID;

public interface PostService {
    List<Post> getAllPosts(UUID categoryId, UUID tagId);
    List<Post> getDraftPosts(User user);
    Post createPost(User user, CreatePostRequest createPostRequest);

    Post updatePost(UUID id, UpdatePostRequest updatePostRequest, UUID userId);

    Post getPost(UUID id);

    void deletePost(UUID id, UUID userId);
}
