package com.saud.blog.Services.impl;

import com.saud.blog.Repositories.PostRepository;
import com.saud.blog.Services.CategoryService;
import com.saud.blog.Services.PostService;
import com.saud.blog.Services.TagsService;
import com.saud.blog.domain.CreatePostRequest;
import com.saud.blog.domain.PostStatus;
import com.saud.blog.domain.UpdatePostRequest;
import com.saud.blog.domain.entities.Category;
import com.saud.blog.domain.entities.Post;
import com.saud.blog.domain.entities.Tag;
import com.saud.blog.domain.entities.User;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PostServiceImpl implements PostService {
    private final PostRepository postRepository;
    private final CategoryService categoryService;
    private final TagsService tagsService;
    private static final  int Words_Per_Minute = 200;

    @Override
    @Transactional(readOnly = true)
    public List<Post> getAllPosts(UUID categoryId, UUID tagId) {
        if(categoryId!=null && tagId!=null) {
            Category category = categoryService.getCategoryById(categoryId);
            Tag tag = tagsService.getTagById(tagId);

            postRepository.findAllByStatusAndCategoryAndTagsContaining(
                    PostStatus.PUBLISHED,
                    category,
                    tag
            );
        }

        if(categoryId != null) {
            Category category = categoryService.getCategoryById(categoryId);
            return postRepository.findAllByStatusAndCategory(
                    PostStatus.PUBLISHED,
                    category
            );
        }

        if(tagId != null) {
            Tag tag = tagsService.getTagById(tagId);
            return postRepository.findAllByStatusAndTagsContaining(
                    PostStatus.PUBLISHED,
                    tag
            );
        }

        return postRepository.findAllByStatus(PostStatus.PUBLISHED);


    }

    @Override
    public List<Post> getDraftPosts(User user) {
        return postRepository.findAllByAuthorAndStatus(user, PostStatus.DRAFT);
    }

    @Override
    @Transactional
    public Post createPost(User user, CreatePostRequest createPostRequest) {
        Post newPost = new Post();
        newPost.setTitle(createPostRequest.getTitle());
        newPost.setContent(createPostRequest.getContent());
        System.out.println("Status being set: " + createPostRequest.getStatus());
        newPost.setStatus(createPostRequest.getStatus());
        System.out.println("Post status after set: " + newPost.getStatus());
        newPost.setAuthor(user);
        newPost.setReadingTime(calculateReadingTime(createPostRequest.getContent()));

        Category category = categoryService.getCategoryById(createPostRequest.getCategoryId());
        newPost.setCategory(category);

        Set<UUID> tagIds = createPostRequest.getTagIds();
        List<Tag> tags = tagsService.getTagByIds(tagIds);
        newPost.setTags(new HashSet<>(tags));

        return postRepository.save(newPost);
    }


    @Override
    @Transactional
    public Post updatePost(UUID id, UpdatePostRequest updatePostRequest, UUID requestingUserId) {
        Post existingPost = postRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Post does not exist with Id " + id));

        // ← add this check
        if (!existingPost.getAuthor().getId().equals(requestingUserId)) {
            throw new AccessDeniedException("You are not allowed to edit this post");
        }

        existingPost.setTitle(updatePostRequest.getTitle());
        existingPost.setContent(updatePostRequest.getContent());
        existingPost.setStatus(updatePostRequest.getStatus());
        existingPost.setReadingTime(calculateReadingTime(updatePostRequest.getContent()));

        UUID updatePostRequestCategoryId = updatePostRequest.getCategoryId();
        if (!existingPost.getCategory().getId().equals(updatePostRequestCategoryId)) {
            Category newCategory = categoryService.getCategoryById(updatePostRequestCategoryId);
            existingPost.setCategory(newCategory);
        }

        Set<UUID> existingTagIds = existingPost.getTags().stream()
                .map(Tag::getId).collect(Collectors.toSet());
        Set<UUID> updatePostRequestTagIds = updatePostRequest.getTagIds();
        if (!existingTagIds.equals(updatePostRequestTagIds)) {
            List<Tag> newTags = tagsService.getTagByIds(updatePostRequestTagIds);
            existingPost.setTags(new HashSet<>(newTags));
        }

        return postRepository.save(existingPost);
    }

    @Override
    public Post getPost(UUID id) {
        return postRepository.findById(id).orElseThrow(()-> new EntityNotFoundException("Post does not exist with id "+ id));
    }

    @Override
    public void deletePost(UUID id, UUID requestingUserId) {  // ← add userId param
        Post post = getPost(id);
        // ← add this check
        if (!post.getAuthor().getId().equals(requestingUserId)) {
            throw new AccessDeniedException("You are not allowed to delete this post");
        }

        postRepository.deleteById(id);
    }

    private Integer calculateReadingTime(String content) {
        if(content == null ||  content.isEmpty()) {
            return 0;
        }

        int wordCount = content.trim().split("\\s+").length;
        return (int) Math.ceil((double)wordCount/Words_Per_Minute);





    }
}
