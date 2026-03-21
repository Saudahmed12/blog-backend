package com.saud.blog.Services;

import com.saud.blog.domain.entities.Category;
import org.w3c.dom.stylesheets.LinkStyle;

import java.util.List;
import java.util.UUID;

public interface CategoryService {
    List<Category> listCategories();

    Category createCategory(Category category);

    void deleteCategory(UUID id);

    Category getCategoryById(UUID Id);
}
