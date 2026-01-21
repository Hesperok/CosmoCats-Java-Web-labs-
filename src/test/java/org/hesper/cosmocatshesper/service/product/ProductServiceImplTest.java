package org.hesper.cosmocatshesper.service.product;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.hesper.cosmocatshesper.domain.category.Category;
import org.hesper.cosmocatshesper.domain.product.Product;
import org.hesper.cosmocatshesper.service.category.CategoryService;
import org.hesper.cosmocatshesper.web.error.CategoryNotFoundException;
import org.hesper.cosmocatshesper.web.error.NotFoundException;
import org.hesper.cosmocatshesper.web.error.ProductNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

@SpringBootTest(classes = ProductServiceImpl.class)
@DisplayName("ProductServiceImpl (SpringBootTest)")
class ProductServiceImplTest {

    @Autowired
    private ProductServiceImpl productService;

    @MockBean
    private CategoryService categoryService;

    @BeforeEach
    void resetStorage() {
        productService.clearStorage();
        clearInvocations(categoryService);
    }

    @Test
    @DisplayName("create: should create product, generate id, and resolve category")
    void create_shouldCreateProduct() {
        UUID categoryId = UUID.randomUUID();
        Category resolvedCategory = Category.builder()
                .id(categoryId)
                .name("Space Food")
                .build();

        when(categoryService.findCategoryById(categoryId)).thenReturn(Optional.of(resolvedCategory));

        Product draft = Product.builder()
                .name("Galaxy Milk")
                .description("Fresh milk from Andromeda")
                .price(new BigDecimal("19.99"))
                .category(Category.builder().id(categoryId).name(null).build())
                .available(true)
                .build();

        Product created = productService.createProduct(draft);

        assertThat(created.getId()).isNotNull();
        assertThat(created.getName()).isEqualTo("Galaxy Milk");
        assertThat(created.getDescription()).isEqualTo("Fresh milk from Andromeda");
        assertThat(created.getPrice()).isEqualByComparingTo("19.99");
        assertThat(created.isAvailable()).isTrue();

        assertThat(created.getCategory()).isNotNull();
        assertThat(created.getCategory().getId()).isEqualTo(categoryId);
        assertThat(created.getCategory().getName()).isEqualTo("Space Food");

        verify(categoryService, times(1)).findCategoryById(categoryId);
        verifyNoMoreInteractions(categoryService);
    }

    @Test
    @DisplayName("create: should throw CategoryNotFoundException when category does not exist")
    void create_shouldThrowCategoryNotFound() {
        UUID categoryId = UUID.randomUUID();
        when(categoryService.findCategoryById(categoryId)).thenReturn(Optional.empty());

        Product draft = Product.builder()
                .name("Galaxy Milk")
                .description("Fresh milk from Andromeda")
                .price(new BigDecimal("19.99"))
                .category(Category.builder().id(categoryId).name(null).build())
                .available(true)
                .build();

        assertThatThrownBy(() -> productService.createProduct(draft))
                .isInstanceOf(CategoryNotFoundException.class)
                .hasMessage(String.format(NotFoundException.ID_NOT_FOUND_TEMPLATE, "Category", categoryId));

        verify(categoryService, times(1)).findCategoryById(categoryId);
        verifyNoMoreInteractions(categoryService);
    }

    @Test
    @DisplayName("getAll: should return all created products")
    void getAll_shouldReturnAllProducts() {
        UUID categoryId = UUID.randomUUID();
        Category resolvedCategory = Category.builder().id(categoryId).name("Space Food").build();
        when(categoryService.findCategoryById(categoryId)).thenReturn(Optional.of(resolvedCategory));

        Product created1 = productService.createProduct(Product.builder()
                .name("Galaxy Milk")
                .description("Milk")
                .price(new BigDecimal("19.99"))
                .category(Category.builder().id(categoryId).name(null).build())
                .available(true)
                .build());

        Product created2 = productService.createProduct(Product.builder()
                .name("Star Snack")
                .description("Snack")
                .price(new BigDecimal("5.50"))
                .category(Category.builder().id(categoryId).name(null).build())
                .available(false)
                .build());

        List<Product> all = productService.getAllProducts();

        assertThat(all).hasSize(2);

        Product p1 = all.stream()
                .filter(p -> p.getId().equals(created1.getId()))
                .findFirst()
                .orElseThrow();

        assertThat(p1.getId()).isEqualTo(created1.getId());
        assertThat(p1.getName()).isEqualTo("Galaxy Milk");
        assertThat(p1.getDescription()).isEqualTo("Milk");
        assertThat(p1.getPrice()).isEqualByComparingTo("19.99");
        assertThat(p1.isAvailable()).isTrue();
        assertThat(p1.getCategory()).isNotNull();
        assertThat(p1.getCategory().getId()).isEqualTo(categoryId);
        assertThat(p1.getCategory().getName()).isEqualTo("Space Food");

        Product p2 = all.stream()
                .filter(p -> p.getId().equals(created2.getId()))
                .findFirst()
                .orElseThrow();

        assertThat(p2.getId()).isEqualTo(created2.getId());
        assertThat(p2.getName()).isEqualTo("Star Snack");
        assertThat(p2.getDescription()).isEqualTo("Snack");
        assertThat(p2.getPrice()).isEqualByComparingTo("5.50");
        assertThat(p2.isAvailable()).isFalse();
        assertThat(p2.getCategory()).isNotNull();
        assertThat(p2.getCategory().getId()).isEqualTo(categoryId);
        assertThat(p2.getCategory().getName()).isEqualTo("Space Food");

        verify(categoryService, times(2)).findCategoryById(categoryId);
        verifyNoMoreInteractions(categoryService);
    }

    @Test
    @DisplayName("getById: should return product when exists")
    void getById_shouldReturnExistingProduct() {
        UUID categoryId = UUID.randomUUID();
        Category resolvedCategory = Category.builder().id(categoryId).name("Space Food").build();
        when(categoryService.findCategoryById(categoryId)).thenReturn(Optional.of(resolvedCategory));

        Product created = productService.createProduct(Product.builder()
                .name("Comet Drink")
                .description("Drink")
                .price(new BigDecimal("3.00"))
                .category(Category.builder().id(categoryId).name(null).build())
                .available(true)
                .build());

        Product found = productService.getProductById(created.getId());

        assertThat(found.getId()).isEqualTo(created.getId());
        assertThat(found.getName()).isEqualTo("Comet Drink");
        assertThat(found.getDescription()).isEqualTo("Drink");
        assertThat(found.getPrice()).isEqualByComparingTo("3.00");
        assertThat(found.isAvailable()).isTrue();
        assertThat(found.getCategory()).isNotNull();
        assertThat(found.getCategory().getId()).isEqualTo(categoryId);
        assertThat(found.getCategory().getName()).isEqualTo("Space Food");

        verify(categoryService, times(1)).findCategoryById(categoryId);
        verifyNoMoreInteractions(categoryService);
    }

    @Test
    @DisplayName("getById: should throw ProductNotFoundException when missing")
    void getById_shouldThrowWhenMissing() {
        UUID missingId = UUID.randomUUID();

        assertThatThrownBy(() -> productService.getProductById(missingId))
                .isInstanceOf(ProductNotFoundException.class)
                .hasMessage(String.format(NotFoundException.ID_NOT_FOUND_TEMPLATE, "Product", missingId));

        verifyNoInteractions(categoryService);
    }

    @Test
    @DisplayName("update: should update existing product and resolve category")
    void update_shouldUpdateExistingProduct() {
        UUID categoryId = UUID.randomUUID();
        Category resolvedCategory = Category.builder().id(categoryId).name("Space Food").build();
        when(categoryService.findCategoryById(categoryId)).thenReturn(Optional.of(resolvedCategory));

        Product created = productService.createProduct(Product.builder()
                .name("Old Galaxy Milk")
                .description("Old")
                .price(new BigDecimal("1.00"))
                .category(Category.builder().id(categoryId).name(null).build())
                .available(true)
                .build());

        Product updateDraft = Product.builder()
                .name("New Galaxy Milk")
                .description("New")
                .price(new BigDecimal("2.00"))
                .category(Category.builder().id(categoryId).name(null).build())
                .available(false)
                .build();

        Product updated = productService.updateProduct(created.getId(), updateDraft);

        assertThat(updated.getId()).isEqualTo(created.getId());
        assertThat(updated.getName()).isEqualTo("New Galaxy Milk");
        assertThat(updated.getDescription()).isEqualTo("New");
        assertThat(updated.getPrice()).isEqualByComparingTo("2.00");
        assertThat(updated.isAvailable()).isFalse();
        assertThat(updated.getCategory()).isNotNull();
        assertThat(updated.getCategory().getId()).isEqualTo(categoryId);
        assertThat(updated.getCategory().getName()).isEqualTo("Space Food");

        verify(categoryService, times(2)).findCategoryById(categoryId);
        verifyNoMoreInteractions(categoryService);
    }

    @Test
    @DisplayName("update: should throw ProductNotFoundException when product missing (and not call CategoryService)")
    void update_shouldThrowProductNotFoundWhenMissing() {
        UUID missingProductId = UUID.randomUUID();
        UUID categoryId = UUID.randomUUID();

        Product updateDraft = Product.builder()
                .name("Whatever")
                .description("Desc")
                .price(new BigDecimal("2.00"))
                .category(Category.builder().id(categoryId).name(null).build())
                .available(true)
                .build();

        assertThatThrownBy(() -> productService.updateProduct(missingProductId, updateDraft))
                .isInstanceOf(ProductNotFoundException.class)
                .hasMessage(String.format(NotFoundException.ID_NOT_FOUND_TEMPLATE, "Product", missingProductId));

        verifyNoInteractions(categoryService);
    }

    @Test
    @DisplayName("update: should throw CategoryNotFoundException when category missing")
    void update_shouldThrowCategoryNotFound() {
        UUID existingCategoryId = UUID.randomUUID();
        Category resolvedCategory = Category.builder().id(existingCategoryId).name("Space Food").build();
        when(categoryService.findCategoryById(existingCategoryId)).thenReturn(Optional.of(resolvedCategory));

        Product created = productService.createProduct(Product.builder()
                .name("Galaxy Milk")
                .description("Desc")
                .price(new BigDecimal("1.00"))
                .category(Category.builder().id(existingCategoryId).name(null).build())
                .available(true)
                .build());

        UUID missingCategoryId = UUID.randomUUID();
        when(categoryService.findCategoryById(missingCategoryId)).thenReturn(Optional.empty());

        Product updateDraft = Product.builder()
                .name("Galaxy Milk Updated")
                .description("Desc2")
                .price(new BigDecimal("2.00"))
                .category(Category.builder().id(missingCategoryId).name(null).build())
                .available(true)
                .build();

        assertThatThrownBy(() -> productService.updateProduct(created.getId(), updateDraft))
                .isInstanceOf(CategoryNotFoundException.class)
                .hasMessage(String.format(NotFoundException.ID_NOT_FOUND_TEMPLATE, "Category", missingCategoryId));

        verify(categoryService, times(1)).findCategoryById(existingCategoryId);
        verify(categoryService, times(1)).findCategoryById(missingCategoryId);
        verifyNoMoreInteractions(categoryService);
    }

    @Test
    @DisplayName("deleteById: should be idempotent and remove existing product")
    void deleteById_shouldBeIdempotent() {
        UUID categoryId = UUID.randomUUID();
        Category resolvedCategory = Category.builder().id(categoryId).name("Space Food").build();
        when(categoryService.findCategoryById(categoryId)).thenReturn(Optional.of(resolvedCategory));

        Product created = productService.createProduct(Product.builder()
                .name("Nebula Snack")
                .description("Snack")
                .price(new BigDecimal("3.00"))
                .category(Category.builder().id(categoryId).name(null).build())
                .available(true)
                .build());

        assertThatCode(() -> productService.deleteProductById(created.getId())).doesNotThrowAnyException();
        assertThatThrownBy(() -> productService.getProductById(created.getId()))
                .isInstanceOf(ProductNotFoundException.class);

        assertThatCode(() -> productService.deleteProductById(created.getId())).doesNotThrowAnyException();
        assertThatCode(() -> productService.deleteProductById(UUID.randomUUID())).doesNotThrowAnyException();

        verify(categoryService, times(1)).findCategoryById(categoryId);
        verifyNoMoreInteractions(categoryService);
    }
}
