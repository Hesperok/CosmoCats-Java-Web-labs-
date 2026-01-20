package org.hesper.cosmocatshesper.dto.category;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Value;

@Value
public class CategoryCreateRequestDto {
    @NotBlank
    @Size(min = 2, max = 60)
    String name;
}
