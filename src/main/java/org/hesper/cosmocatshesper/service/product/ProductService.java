package org.hesper.cosmocatshesper.service.product;

import java.util.List;
import java.util.UUID;
import org.hesper.cosmocatshesper.domain.product.Product;

public interface ProductService {
    Product createProduct(Product draft);
    List<Product> getAllProducts();
    Product getProductById(UUID id);
    Product updateProduct(UUID id, Product draft);
    void deleteProductById(UUID id);
}
