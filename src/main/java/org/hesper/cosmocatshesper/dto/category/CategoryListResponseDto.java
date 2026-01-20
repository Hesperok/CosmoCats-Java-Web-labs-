package org.hesper.cosmocatshesper.dto.category;

import java.util.List;
import lombok.Value;

@Value
public class CategoryListResponseDto {
    List<CategoryResponseDto> items;
}
