package org.hesper.cosmocatshesper.service.category;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.hesper.cosmocatshesper.domain.category.Category;

public interface CategoryService {
    Category createCategory(Category draft);
    List<Category> getAllCategories();
    Optional<Category> findCategoryById(UUID id);
    void deleteCategoryById(UUID id);
}
