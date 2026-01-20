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
@RequestMapping("/api/products")
@RequiredArgsConstructor
public class ProductController {

    private final ProductService productService;
    private final ProductMapper productMapper;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ProductResponseDto create(@Valid @RequestBody ProductUpsertRequestDto dto) {
        Product draft = productMapper.mapUpsertRequestDtoToProductCreateDraft(dto);
        Product created = productService.create(draft);
        return productMapper.mapProductToResponseDto(created);
    }

    @GetMapping
    public ProductListResponseDto list() {
        List<ProductResponseDto> items = productService.getAll()
                .stream()
                .map(productMapper::mapProductToResponseDto)
                .toList();

        return new ProductListResponseDto(items);
    }

    @GetMapping("/{id}")
    public ProductResponseDto getById(@PathVariable UUID id) {
        Product product = productService.getById(id);
        return productMapper.mapProductToResponseDto(product);
    }

    @PutMapping("/{id}")
    public ProductResponseDto update(@PathVariable UUID id, @Valid @RequestBody ProductUpsertRequestDto dto) {
        Product draft = productMapper.mapUpsertRequestDtoToProductUpdateDraft(id, dto);
        Product updated = productService.update(id, draft);
        return productMapper.mapProductToResponseDto(updated);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable UUID id) {
        productService.deleteById(id);
    }
}
