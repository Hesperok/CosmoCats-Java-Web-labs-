package org.hesper.cosmocatshesper.web.product;

import jakarta.validation.Valid;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.hesper.cosmocatshesper.domain.product.Product;
import org.hesper.cosmocatshesper.dto.product.ProductListResponseDto;
import org.hesper.cosmocatshesper.dto.product.ProductResponseDto;
import org.hesper.cosmocatshesper.dto.product.ProductUpsertRequestDto;
import org.hesper.cosmocatshesper.service.product.ProductService;
import org.hesper.cosmocatshesper.util.mapper.ProductMapper;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/products")
@RequiredArgsConstructor
public class ProductController {

    private final ProductService productService;
    private final ProductMapper productMapper;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ProductResponseDto createProduct(@Valid @RequestBody ProductUpsertRequestDto dto) {
        Product draft = productMapper.mapUpsertRequestDtoToProductCreateDraft(dto);
        Product created = productService.createProduct(draft);
        return productMapper.mapProductToResponseDto(created);
    }

    @GetMapping
    public ProductListResponseDto listProducts() {
        List<ProductResponseDto> items = productService.getAllProducts()
                .stream()
                .map(productMapper::mapProductToResponseDto)
                .toList();

        return new ProductListResponseDto(items);
    }

    @GetMapping("/{id}")
    public ProductResponseDto getProductById(@PathVariable UUID id) {
        Product product = productService.getProductById(id);
        return productMapper.mapProductToResponseDto(product);
    }

    @PutMapping("/{id}")
    public ProductResponseDto updateProduct(@PathVariable UUID id, @Valid @RequestBody ProductUpsertRequestDto dto) {
        Product draft = productMapper.mapUpsertRequestDtoToProductUpdateDraft(id, dto);
        Product updated = productService.updateProduct(id, draft);
        return productMapper.mapProductToResponseDto(updated);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteProduct(@PathVariable UUID id) {
        productService.deleteProductById(id);
    }
}
