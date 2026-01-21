package org.hesper.cosmocatshesper.util.mapper;

import org.hesper.cosmocatshesper.domain.category.Category;
import org.hesper.cosmocatshesper.dto.category.CategoryCreateRequestDto;
import org.hesper.cosmocatshesper.dto.category.CategoryResponseDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface CategoryMapper {

    @Mapping(target = "id", ignore = true)
    Category mapCreateRequestDtoToCategoryDraft(CategoryCreateRequestDto dto);

    CategoryResponseDto mapCategoryToResponseDto(Category category);
}
