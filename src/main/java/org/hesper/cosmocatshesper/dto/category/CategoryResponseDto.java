package org.hesper.cosmocatshesper.dto.category;

import java.util.UUID;
import lombok.Value;

@Value
public class CategoryResponseDto {
    UUID id;
    String name;
}
