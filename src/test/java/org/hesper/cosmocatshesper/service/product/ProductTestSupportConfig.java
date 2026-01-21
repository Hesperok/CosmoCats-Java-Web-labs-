package org.hesper.cosmocatshesper.service.product;

import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;

@TestConfiguration
public class ProductTestSupportConfig {

    @Bean
    public ProductTestSupport productTestSupport(ProductServiceImpl productService) {
        return new ProductTestSupport(productService);
    }

    public static class ProductTestSupport {
        private final ProductServiceImpl productService;

        public ProductTestSupport(ProductServiceImpl productService) {
            this.productService = productService;
        }

        public void clear() {
            productService.clearStorage();
        }
    }
}
