package org.hesper.cosmocatshesper.web.error;

import java.util.UUID;

public class ProductNotFoundException extends NotFoundException {

    public ProductNotFoundException(UUID id) {
        super("Product", id);
    }

}
