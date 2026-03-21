package com.saud.blog.Mappers;

import com.saud.blog.domain.CreatePostRequest;
import com.saud.blog.domain.DTOs.CreatePostRequestDto;
import com.saud.blog.domain.DTOs.PostDto;
import com.saud.blog.domain.DTOs.UpdatePostRequestDto;
import com.saud.blog.domain.UpdatePostRequest;
import com.saud.blog.domain.entities.Post;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface PostMapper {

    @Mapping(target = "author", source = "author")
    @Mapping(target = "category", source = "category")
    @Mapping(target = "tags", source = "tags")
    PostDto toDto(Post post);

    CreatePostRequest toCreatePostRequest(CreatePostRequestDto dto);

    UpdatePostRequest toUpdatePostRequest(UpdatePostRequestDto dto);
}
