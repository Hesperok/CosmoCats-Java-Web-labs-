package org.hesper.cosmocatshesper.dto.category;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Value;

@Value
public class CategoryCreateRequestDto {
    @NotBlank
    @Size(min = 2, max = 60)
    String name;

    @JsonCreator(mode = JsonCreator.Mode.PROPERTIES)
    public CategoryCreateRequestDto(@JsonProperty("name") String name) {
        this.name = name;
    }
}
