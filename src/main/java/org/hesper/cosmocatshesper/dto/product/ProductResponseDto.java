package org.hesper.cosmocatshesper.dto.product;

import java.math.BigDecimal;
import java.util.UUID;
import lombok.Value;

@Value
public class ProductResponseDto {
    UUID id;
    String name;
    String description;
    BigDecimal price;
    UUID categoryId;
    String categoryName;
    Boolean available;
}
