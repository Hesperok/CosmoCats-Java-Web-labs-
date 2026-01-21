package org.hesper.cosmocatshesper.web.category;

import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.Map;
import java.util.UUID;
import lombok.SneakyThrows;
import org.hesper.cosmocatshesper.service.category.CategoryTestSupportConfig;
import org.hesper.cosmocatshesper.service.category.CategoryTestSupportConfig.CategoryTestSupport;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

@SpringBootTest
@AutoConfigureMockMvc
@Import(CategoryTestSupportConfig.class)
@DisplayName("CategoryController integration tests (full validation)")
class CategoryControllerIT {

    @Autowired private MockMvc mockMvc;
    @Autowired private ObjectMapper objectMapper;

    @Autowired private CategoryTestSupport categoryTestSupport;

    @BeforeEach
    void setUp() {
        categoryTestSupport.clear();
    }

    @Test
    @SneakyThrows
    @DisplayName("should create category (201 Created)")
    void shouldCreateCategory() {
        Map<String, Object> request = Map.of("name", "Space Food");

        mockMvc.perform(post("/api/v1/categories")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id", not(emptyOrNullString())))
                .andExpect(jsonPath("$.name").value("Space Food"));
    }

    @Test
    @SneakyThrows
    @DisplayName("should return 400 when name blank (NotBlank)")
    void shouldReturn400_whenNameBlank() {
        Map<String, Object> request = Map.of("name", " ");

        mockMvc.perform(post("/api/v1/categories")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_PROBLEM_JSON))
                .andExpect(jsonPath("$.title").value("Bad Request"))
                .andExpect(jsonPath("$.status").value(400))
                .andExpect(jsonPath("$.detail").value("Validation failed"))
                .andExpect(jsonPath("$.path").value("/api/v1/categories"))
                .andExpect(jsonPath("$.errors").isArray())
                .andExpect(jsonPath("$.errors[*].field", hasItem("name")))
                .andExpect(jsonPath("$.errors[?(@.field=='name')].message",
                        hasItem(containsString("must not be blank"))));
    }

    @Test
    @SneakyThrows
    @DisplayName("should return 400 when name too short (Size)")
    void shouldReturn400_whenNameTooShort() {
        Map<String, Object> request = Map.of("name", "A");

        mockMvc.perform(post("/api/v1/categories")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_PROBLEM_JSON))
                .andExpect(jsonPath("$.title").value("Bad Request"))
                .andExpect(jsonPath("$.status").value(400))
                .andExpect(jsonPath("$.detail").value("Validation failed"))
                .andExpect(jsonPath("$.path").value("/api/v1/categories"))
                .andExpect(jsonPath("$.errors").isArray())
                .andExpect(jsonPath("$.errors[*].field", hasItem("name")))
                .andExpect(jsonPath("$.errors[?(@.field=='name')].message",
                        hasItem(containsString("size must be between 2 and 60"))));
    }

    @Test
    @SneakyThrows
    @DisplayName("should return 400 when name too long (Size)")
    void shouldReturn400_whenNameTooLong() {
        String longName = "A".repeat(61);
        Map<String, Object> request = Map.of("name", longName);

        mockMvc.perform(post("/api/v1/categories")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_PROBLEM_JSON))
                .andExpect(jsonPath("$.title").value("Bad Request"))
                .andExpect(jsonPath("$.status").value(400))
                .andExpect(jsonPath("$.detail").value("Validation failed"))
                .andExpect(jsonPath("$.path").value("/api/v1/categories"))
                .andExpect(jsonPath("$.errors").isArray())
                .andExpect(jsonPath("$.errors[*].field", hasItem("name")))
                .andExpect(jsonPath("$.errors[?(@.field=='name')].message",
                        hasItem(containsString("size must be between 2 and 60"))));
    }

    @Test
    @SneakyThrows
    @DisplayName("should list categories (200 OK)")
    void shouldListCategories() {
        createCategory("Space Food");
        createCategory("Cosmic Toys");

        mockMvc.perform(get("/api/v1/categories"))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.items").isArray())
                .andExpect(jsonPath("$.items", hasSize(2)))
                .andExpect(jsonPath("$.items[*].id", everyItem(not(emptyOrNullString()))))
                .andExpect(jsonPath("$.items[*].name", hasItems("Space Food", "Cosmic Toys")));
    }

    @Test
    @SneakyThrows
    @DisplayName("should delete category (204 No Content) and always return 204 on delete")
    void shouldDeleteCategoryAlways204() {
        UUID id = createCategory("Space Food");

        mockMvc.perform(delete("/api/v1/categories/{id}", id))
                .andExpect(status().isNoContent());

        mockMvc.perform(delete("/api/v1/categories/{id}", id))
                .andExpect(status().isNoContent());

        mockMvc.perform(delete("/api/v1/categories/{id}", UUID.randomUUID()))
                .andExpect(status().isNoContent());
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
}
