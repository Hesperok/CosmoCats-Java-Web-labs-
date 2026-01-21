package org.hesper.cosmocatshesper.service.product;

import java.util.List;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import lombok.RequiredArgsConstructor;
import org.hesper.cosmocatshesper.domain.category.Category;
import org.hesper.cosmocatshesper.domain.product.Product;
import org.hesper.cosmocatshesper.service.category.CategoryService;
import org.hesper.cosmocatshesper.web.error.CategoryNotFoundException;
import org.hesper.cosmocatshesper.web.error.ProductNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ProductServiceImpl implements ProductService {

    private final CategoryService categoryService;
    private final ConcurrentHashMap<UUID, Product> storage = new ConcurrentHashMap<>();

    @Override
    public Product create(Product draft) {
        UUID categoryId = draft.getCategory().getId();
        Category category = categoryService.findById(categoryId)
                .orElseThrow(() -> new CategoryNotFoundException(categoryId));

        UUID id = UUID.randomUUID();
        Product created = draft.toBuilder()
                .id(id)
                .category(category)
                .build();

        storage.put(id, created);
        return created;
    }

    @Override
    public List<Product> getAll() {
        return storage.values().stream().toList();
    }

    @Override
    public Product getById(UUID id) {
        Product product = storage.get(id);
        if (product == null) {
            throw new ProductNotFoundException(id);
        }
        return product;
    }

    @Override
    public Product update(UUID id, Product draft) {
        if (!storage.containsKey(id)) {
            throw new ProductNotFoundException(id);
        }

        UUID categoryId = draft.getCategory().getId();
        Category category = categoryService.findById(categoryId)
                .orElseThrow(() -> new CategoryNotFoundException(categoryId));

        Product updated = draft.toBuilder()
                .id(id)
                .category(category)
                .build();

        storage.put(id, updated);
        return updated;
    }

    @Override
    public void deleteById(UUID id) {
        storage.remove(id);
    }

    void clearStorage() {
        storage.clear();
    }
}
