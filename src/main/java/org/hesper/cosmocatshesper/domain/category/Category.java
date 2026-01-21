package org.hesper.cosmocatshesper.domain.category;

import java.util.UUID;
import lombok.Builder;
import lombok.Value;

@Value
@Builder(toBuilder = true)
public class Category {
    UUID id;
    String name;
}
