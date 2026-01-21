package org.hesper.cosmocatshesper.dto.product;

import java.util.List;
import lombok.Value;

@Value
public class ProductListResponseDto {
    List<ProductResponseDto> items;
}
