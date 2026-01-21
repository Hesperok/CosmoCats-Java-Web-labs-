package org.hesper.cosmocatshesper.dto.product;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.math.BigDecimal;
import java.util.UUID;
import lombok.Value;
import org.hesper.cosmocatshesper.dto.validation.CosmicWordCheck;

@Value
public class ProductUpsertRequestDto {
    @NotBlank
    @Size(min = 2, max = 80)
    @CosmicWordCheck
    String name;

    @Size(max = 500)
    String description;

    @NotNull
    @DecimalMin(value = "0.01", inclusive = true)
    BigDecimal price;

    @NotNull
    UUID categoryId;

    @NotNull
    Boolean available;
}
