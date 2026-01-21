package org.hesper.cosmocatshesper.service.category;

import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;

@TestConfiguration
public class CategoryTestSupportConfig {

    @Bean
    CategoryTestSupport categoryTestSupport(CategoryServiceImpl categoryService) {
        return new CategoryTestSupport(categoryService);
    }

    public static class CategoryTestSupport {
        private final CategoryServiceImpl categoryService;

        CategoryTestSupport(CategoryServiceImpl categoryService) {
            this.categoryService = categoryService;
        }

        public void clear() {
            categoryService.clearStorage();
        }
    }
}
