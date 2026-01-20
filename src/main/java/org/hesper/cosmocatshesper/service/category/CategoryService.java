package org.hesper.cosmocatshesper.service.category;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.hesper.cosmocatshesper.domain.category.Category;

public interface CategoryService {
    Category create(Category draft);
    List<Category> getAll();
    Optional<Category> findById(UUID id);
    void deleteById(UUID id);
}
