package org.hesper.cosmocatshesper.service.product;

import java.util.List;
import java.util.UUID;
import org.hesper.cosmocatshesper.domain.product.Product;

public interface ProductService {
    Product create(Product draft);
    List<Product> getAll();
    Product getById(UUID id);
    Product update(UUID id, Product draft);
    void deleteById(UUID id);
}
