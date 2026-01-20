package org.hesper.cosmocatshesper.web.error;

import java.util.UUID;

public abstract class NotFoundException extends RuntimeException {

    public static final String ID_NOT_FOUND_TEMPLATE = "%s with id %s was not found";

    protected NotFoundException(String objectType, UUID id) {
        super(String.format(ID_NOT_FOUND_TEMPLATE, objectType, id));
    }

    protected NotFoundException(String message) {
        super(message);
    }
}
