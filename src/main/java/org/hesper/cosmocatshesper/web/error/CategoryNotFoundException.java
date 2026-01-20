package org.hesper.cosmocatshesper.web.error;

import java.util.UUID;

public class CategoryNotFoundException extends NotFoundException {

    public CategoryNotFoundException(UUID id) {
        super("Category", id);
    }

    public CategoryNotFoundException(String message) {
        super(message);
    }
}
