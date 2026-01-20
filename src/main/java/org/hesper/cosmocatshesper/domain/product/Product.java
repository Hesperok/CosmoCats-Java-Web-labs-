package org.hesper.cosmocatshesper.domain.product;

import java.math.BigDecimal;
import java.util.UUID;
import lombok.Builder;
import lombok.Value;
import org.hesper.cosmocatshesper.domain.category.Category;

@Value
@Builder(toBuilder = true)
public class Product {
    UUID id;
    String name;
    String description;
    BigDecimal price;
    Category category;
    boolean available;
}
