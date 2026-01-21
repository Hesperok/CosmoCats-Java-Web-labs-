package org.hesper.cosmocatshesper.service.category;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import org.hesper.cosmocatshesper.domain.category.Category;
import org.springframework.stereotype.Service;

@Service
public class CategoryServiceImpl implements CategoryService {

    private final ConcurrentHashMap<UUID, Category> storage = new ConcurrentHashMap<>();

    @Override
    public Category create(Category draft) {
        UUID id = UUID.randomUUID();
        Category created = draft.toBuilder().id(id).build();
        storage.put(id, created);
        return created;
    }

    @Override
    public List<Category> getAll() {
        return storage.values().stream().toList();
    }

    @Override
    public Optional<Category> findById(UUID id) {
        return Optional.ofNullable(storage.get(id));
    }

    @Override
    public void deleteById(UUID id) {
        storage.remove(id);
    }

    void clearStorage() {
        storage.clear();
    }
}
