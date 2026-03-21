package com.saud.blog.Controllers;

import com.saud.blog.Mappers.TagMapper;
import com.saud.blog.Services.TagsService;
import com.saud.blog.domain.DTOs.CreateTagRequest;
import com.saud.blog.domain.DTOs.TagDto;
import com.saud.blog.domain.entities.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping(path = "api/v1/tags")
@RequiredArgsConstructor
public class TagController {
    private final TagsService tagsService;
    private final TagMapper mapper;

    @GetMapping
    public ResponseEntity<List<TagDto>> getAllTags() {
        List<Tag> tags= tagsService.getTags();

        List<TagDto> tagRespons = tags.
                stream().
                map(tag -> mapper.toTagResponse(tag)).
                toList();
        return ResponseEntity.ok(tagRespons);
    }

    @PostMapping
    public ResponseEntity<List<TagDto>> createTags(@RequestBody CreateTagRequest createTagRequest) {
        List<Tag> savedTags = tagsService.createTags(createTagRequest.getNames());

        List<TagDto> createTagRespons = savedTags.stream()
                .map(mapper::toTagResponse)
                .toList();
        return new ResponseEntity<>(
                createTagRespons,
                HttpStatus.CREATED
        );
    }

    @DeleteMapping(path = "/{id}")
    public ResponseEntity<Void> deleteTag(@PathVariable UUID id) {
        tagsService.deleteTag(id);
        return ResponseEntity.noContent().build();
    }


}
