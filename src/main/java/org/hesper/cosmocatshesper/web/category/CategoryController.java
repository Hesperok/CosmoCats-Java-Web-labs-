package org.hesper.cosmocatshesper.web.category;

import jakarta.validation.Valid;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.hesper.cosmocatshesper.domain.category.Category;
import org.hesper.cosmocatshesper.dto.category.CategoryCreateRequestDto;
import org.hesper.cosmocatshesper.dto.category.CategoryListResponseDto;
import org.hesper.cosmocatshesper.dto.category.CategoryResponseDto;
import org.hesper.cosmocatshesper.util.mapper.CategoryMapper;
import org.hesper.cosmocatshesper.service.category.CategoryService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/categories")
@RequiredArgsConstructor
public class CategoryController {

    private final CategoryService categoryService;
    private final CategoryMapper categoryMapper;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public CategoryResponseDto createCategory(@Valid @RequestBody CategoryCreateRequestDto dto) {
        Category draft = categoryMapper.mapCreateRequestDtoToCategoryDraft(dto);
        Category created = categoryService.createCategory(draft);
        return categoryMapper.mapCategoryToResponseDto(created);
    }

    @GetMapping
    public CategoryListResponseDto listCategories() {
        List<CategoryResponseDto> items = categoryService.getAllCategories()
                .stream()
                .map(categoryMapper::mapCategoryToResponseDto)
                .toList();

        return new CategoryListResponseDto(items);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteCategory(@PathVariable UUID id) {
        categoryService.deleteCategoryById(id);
    }
}
