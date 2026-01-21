package org.hesper.cosmocatshesper.service.category;

import static org.assertj.core.api.Assertions.*;

import java.util.List;
import java.util.UUID;
import org.hesper.cosmocatshesper.domain.category.Category;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest(classes = CategoryServiceImpl.class)
@DisplayName("CategoryServiceImpl (SpringBootTest)")
class CategoryServiceImplTest {

    @Autowired
    private CategoryServiceImpl categoryService;

    @BeforeEach
    void resetStorage() {
        categoryService.clearStorage();
    }

    @Test
    @DisplayName("create: should create category with generated id")
    void create_shouldCreateCategory() {
        Category draft = Category.builder()
                .id(null)
                .name("Space Food")
                .build();

        Category created = categoryService.create(draft);

        assertThat(created.getId()).isNotNull();
        assertThat(created.getName()).isEqualTo("Space Food");

        List<Category> all = categoryService.getAll();
        assertThat(all).hasSize(1);

        Category stored = all.getFirst();
        assertThat(stored.getId()).isEqualTo(created.getId());
        assertThat(stored.getName()).isEqualTo("Space Food");
    }

    @Test
    @DisplayName("getAll: should return empty list initially")
    void getAll_shouldReturnEmptyInitially() {
        List<Category> all = categoryService.getAll();
        assertThat(all).isEmpty();
    }

    @Test
    @DisplayName("findById: should return Optional.empty when missing")
    void findById_shouldReturnEmptyWhenMissing() {
        UUID missingId = UUID.randomUUID();
        assertThat(categoryService.findById(missingId)).isEmpty();
    }

    @Test
    @DisplayName("findById: should return category when exists")
    void findById_shouldReturnCategoryWhenExists() {
        Category created = categoryService.create(Category.builder()
                .name("Toys")
                .build());

        assertThat(created.getId()).isNotNull();
        assertThat(created.getName()).isEqualTo("Toys");

        Category found = categoryService.findById(created.getId()).orElseThrow();

        assertThat(found.getId()).isEqualTo(created.getId());
        assertThat(found.getName()).isEqualTo("Toys");
    }

    @Test
    @DisplayName("deleteById: should be idempotent and remove existing category")
    void deleteById_shouldBeIdempotent() {
        Category created = categoryService.create(Category.builder()
                .name("Drinks")
                .build());

        assertThat(categoryService.findById(created.getId())).isPresent();

        assertThatCode(() -> categoryService.deleteById(created.getId())).doesNotThrowAnyException();
        assertThat(categoryService.findById(created.getId())).isEmpty();

        assertThatCode(() -> categoryService.deleteById(created.getId())).doesNotThrowAnyException();
        assertThatCode(() -> categoryService.deleteById(UUID.randomUUID())).doesNotThrowAnyException();
    }

    @Test
    @DisplayName("getAll: should return multiple categories")
    void getAll_shouldReturnMultipleCategories() {
        Category c1 = categoryService.create(Category.builder().name("Space Food").build());
        Category c2 = categoryService.create(Category.builder().name("Cosmic Toys").build());

        List<Category> all = categoryService.getAll();
        assertThat(all).hasSize(2);

        Category stored1 = all.stream()
                .filter(c -> c.getId().equals(c1.getId()))
                .findFirst()
                .orElseThrow();

        assertThat(stored1.getId()).isEqualTo(c1.getId());
        assertThat(stored1.getName()).isEqualTo("Space Food");

        Category stored2 = all.stream()
                .filter(c -> c.getId().equals(c2.getId()))
                .findFirst()
                .orElseThrow();

        assertThat(stored2.getId()).isEqualTo(c2.getId());
        assertThat(stored2.getName()).isEqualTo("Cosmic Toys");
    }
}
