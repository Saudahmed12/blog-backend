package com.saud.blog.Services;

import com.saud.blog.domain.entities.Tag;

import java.util.List;
import java.util.Set;
import java.util.UUID;

public interface TagsService {
    List<Tag> getTags();
    List<Tag> createTags(Set<String> tagNames);
    void deleteTag(UUID id);

    Tag getTagById(UUID id);
    List<Tag> getTagByIds(Set<UUID> ids);
}
