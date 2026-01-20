package org.hesper.cosmocatshesper.dto.validation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import java.util.Set;

public class CosmicWordCheckValidator implements ConstraintValidator<CosmicWordCheck, String> {

    private static final Set<String> TERMS = Set.of("star", "galaxy", "comet", "nebula", "cosmic", "orbit");

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        if (value == null) {
            return true;
        }
        String lower = value.toLowerCase();
        return TERMS.stream().anyMatch(lower::contains);
    }
}
