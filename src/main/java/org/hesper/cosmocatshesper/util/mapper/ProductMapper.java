package org.hesper.cosmocatshesper.util.mapper;

import java.util.UUID;
import org.hesper.cosmocatshesper.domain.category.Category;
import org.hesper.cosmocatshesper.domain.product.Product;
import org.hesper.cosmocatshesper.dto.product.ProductResponseDto;
import org.hesper.cosmocatshesper.dto.product.ProductUpsertRequestDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface ProductMapper {

    default Category mapCategoryIdToCategoryReference(UUID categoryId) {
        return Category.builder().id(categoryId).name(null).build();
    }

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "category", expression = "java(mapCategoryIdToCategoryReference(dto.getCategoryId()))")
    @Mapping(target = "available", source = "available")
    Product mapUpsertRequestDtoToProductCreateDraft(ProductUpsertRequestDto dto);

    @Mapping(target = "id", source = "productId")
    @Mapping(target = "category", expression = "java(mapCategoryIdToCategoryReference(dto.getCategoryId()))")
    @Mapping(target = "available", source = "available")
    Product mapUpsertRequestDtoToProductUpdateDraft(UUID productId, ProductUpsertRequestDto dto);

    @Mapping(target = "categoryId", expression = "java(product.getCategory().getId())")
    @Mapping(target = "categoryName", expression = "java(product.getCategory().getName())")
    @Mapping(target = "available", expression = "java(Boolean.valueOf(product.isAvailable()))")
    ProductResponseDto mapProductToResponseDto(Product product);
}
