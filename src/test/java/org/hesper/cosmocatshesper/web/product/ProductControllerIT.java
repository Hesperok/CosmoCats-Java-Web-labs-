package org.hesper.cosmocatshesper.web.product;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.math.BigDecimal;
import java.util.Map;
import java.util.UUID;
import lombok.SneakyThrows;
import org.hamcrest.Matchers;
import org.hesper.cosmocatshesper.service.category.CategoryTestSupportConfig;
import org.hesper.cosmocatshesper.service.category.CategoryTestSupportConfig.CategoryTestSupport;
import org.hesper.cosmocatshesper.service.product.ProductTestSupportConfig;
import org.hesper.cosmocatshesper.service.product.ProductTestSupportConfig.ProductTestSupport;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@Import({ProductTestSupportConfig.class, CategoryTestSupportConfig.class})
@DisplayName("ProductController integration tests (full field coverage)")
class ProductControllerIT {

    @Autowired private MockMvc mockMvc;
    @Autowired private ObjectMapper objectMapper;

    @Autowired private ProductTestSupport productTestSupport;
    @Autowired private CategoryTestSupport categoryTestSupport;

    @BeforeEach
    void setUp() {
        productTestSupport.clear();
        categoryTestSupport.clear();
    }

    @Test
    @SneakyThrows
    @DisplayName("should create product (201 Created)")
    void shouldCreateProduct() {
        UUID categoryId = createCategory("Space Food");

        Map<String, Object> request = Map.of(
                "name", "Galaxy Milk",
                "description", "Fresh milk from the Andromeda sector.",
                "price", new BigDecimal("19.99"),
                "categoryId", categoryId.toString(),
                "available", true
        );

        mockMvc.perform(post("/api/v1/products")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").isNotEmpty())
                .andExpect(jsonPath("$.name").value("Galaxy Milk"))
                .andExpect(jsonPath("$.description").value("Fresh milk from the Andromeda sector."))
                .andExpect(jsonPath("$.price").value(19.99))
                .andExpect(jsonPath("$.categoryId").value(categoryId.toString()))
                .andExpect(jsonPath("$.categoryName").value("Space Food"))
                .andExpect(jsonPath("$.available").value(true));
    }

    @Test
    @SneakyThrows
    @DisplayName("should get product by id (200 OK)")
    void shouldGetProductById() {
        UUID categoryId = createCategory("Space Food");
        UUID productId = createProduct("Galaxy Milk", "Fresh milk", "19.99", categoryId, true);

        mockMvc.perform(get("/api/v1/products/{id}", productId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(productId.toString()))
                .andExpect(jsonPath("$.name").value("Galaxy Milk"))
                .andExpect(jsonPath("$.description").value("Fresh milk"))
                .andExpect(jsonPath("$.price").value(19.99))
                .andExpect(jsonPath("$.categoryId").value(categoryId.toString()))
                .andExpect(jsonPath("$.categoryName").value("Space Food"))
                .andExpect(jsonPath("$.available").value(true));
    }

    @Test
    @SneakyThrows
    @DisplayName("should list products (200 OK)")
    void shouldListProducts() {
        UUID foodId = createCategory("Space Food");
        UUID techId = createCategory("Star Tech");

        UUID p1 = createProduct("Galaxy Milk", "Fresh milk", "19.99", foodId, true);
        UUID p2 = createProduct("Star Phone", "Shiny phone", "999.00", techId, true);

        mockMvc.perform(get("/api/v1/products"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.items").isArray())
                .andExpect(jsonPath("$.items.length()").value(2))

                .andExpect(jsonPath("$.items[?(@.id == '%s')].name".formatted(p1)).value(Matchers.hasItem("Galaxy Milk")))
                .andExpect(jsonPath("$.items[?(@.id == '%s')].categoryId".formatted(p1)).value(Matchers.hasItem(foodId.toString())))
                .andExpect(jsonPath("$.items[?(@.id == '%s')].categoryName".formatted(p1)).value(Matchers.hasItem("Space Food")))
                .andExpect(jsonPath("$.items[?(@.id == '%s')].available".formatted(p1)).value(Matchers.hasItem(true)))

                .andExpect(jsonPath("$.items[?(@.id == '%s')].name".formatted(p2)).value(Matchers.hasItem("Star Phone")))
                .andExpect(jsonPath("$.items[?(@.id == '%s')].categoryId".formatted(p2)).value(Matchers.hasItem(techId.toString())))
                .andExpect(jsonPath("$.items[?(@.id == '%s')].categoryName".formatted(p2)).value(Matchers.hasItem("Star Tech")))
                .andExpect(jsonPath("$.items[?(@.id == '%s')].available".formatted(p2)).value(Matchers.hasItem(true)));
    }

    @Test
    @SneakyThrows
    @DisplayName("should update product (200 OK)")
    void shouldUpdateProduct() {
        UUID oldCategoryId = createCategory("Space Food");
        UUID newCategoryId = createCategory("Star Tech");

        UUID productId = createProduct("Galaxy Milk", "Fresh milk", "19.99", oldCategoryId, true);

        Map<String, Object> update = Map.of(
                "name", "Comet Milk",
                "description", "Updated description",
                "price", new BigDecimal("25.50"),
                "categoryId", newCategoryId.toString(),
                "available", true
        );

        mockMvc.perform(put("/api/v1/products/{id}", productId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(update)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(productId.toString()))
                .andExpect(jsonPath("$.name").value("Comet Milk"))
                .andExpect(jsonPath("$.description").value("Updated description"))
                .andExpect(jsonPath("$.price").value(25.50))
                .andExpect(jsonPath("$.categoryId").value(newCategoryId.toString()))
                .andExpect(jsonPath("$.categoryName").value("Star Tech"))
                .andExpect(jsonPath("$.available").value(true));
    }

    @Test
    @SneakyThrows
    @DisplayName("should delete product (204 No Content) and never return 404 on delete")
    void shouldDeleteProductAlways204() {
        UUID categoryId = createCategory("Space Food");
        UUID productId = createProduct("Galaxy Milk", "Fresh milk", "19.99", categoryId, true);

        mockMvc.perform(delete("/api/v1/products/{id}", productId))
                .andExpect(status().isNoContent());

        mockMvc.perform(delete("/api/v1/products/{id}", productId))
                .andExpect(status().isNoContent());
    }

    @Test
    @SneakyThrows
    @DisplayName("should return 404 when category not found on create")
    void shouldReturnNotFoundWhenCategoryMissingOnCreate() {
        UUID missingCategoryId = UUID.randomUUID();

        Map<String, Object> request = Map.of(
                "name", "Galaxy Milk",
                "description", "Fresh milk",
                "price", new BigDecimal("19.99"),
                "categoryId", missingCategoryId.toString(),
                "available", true
        );

        mockMvc.perform(post("/api/v1/products")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.status").value(404));
    }

    @Test
    @SneakyThrows
    @DisplayName("should return all validation errors for invalid product upsert")
    void shouldReturnValidationErrors() {
        UUID categoryId = createCategory("Space Food");

        Map<String, Object> badRequest = Map.of(
                "name", "",
                "description", "x",
                "price", new BigDecimal("0.00"),
                "categoryId", categoryId.toString(),
                "available", true
        );

        mockMvc.perform(post("/api/v1/products")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(badRequest)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status").value(400))
                .andExpect(jsonPath("$.errors").isArray())
                .andExpect(jsonPath("$.errors[?(@.field == 'name')].message").isNotEmpty())
                .andExpect(jsonPath("$.errors[?(@.field == 'price')].message").isNotEmpty());
    }

    @SneakyThrows
    private UUID createCategory(String name) {
        Map<String, Object> request = Map.of("name", name);

        String response = mockMvc.perform(post("/api/v1/categories")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").isNotEmpty())
                .andExpect(jsonPath("$.name").value(name))
                .andReturn()
                .getResponse()
                .getContentAsString();

        JsonNode node = objectMapper.readTree(response);
        return UUID.fromString(node.get("id").asText());
    }

    @SneakyThrows
    private UUID createProduct(String name, String description, String price, UUID categoryId, boolean available) {
        Map<String, Object> request = Map.of(
                "name", name,
                "description", description,
                "price", new BigDecimal(price),
                "categoryId", categoryId.toString(),
                "available", available
        );

        String response = mockMvc.perform(post("/api/v1/products")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").isNotEmpty())
                .andReturn()
                .getResponse()
                .getContentAsString();

        JsonNode node = objectMapper.readTree(response);
        return UUID.fromString(node.get("id").asText());
    }
}
