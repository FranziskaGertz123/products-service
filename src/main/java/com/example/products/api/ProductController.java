package com.example.products.api;

import com.example.products.api.dto.Message;
import com.example.products.api.dto.ProductDto;
import com.example.products.application.ProductService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/products")
public class ProductController {

    private final ProductService productService;

    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    @GetMapping
    public Message<Page<ProductDto>> getAll(Pageable pageable) {
        Message<Page<ProductDto>> message = new Message<>();
        message.setStatus(HttpStatus.OK);
        message.setContent(productService.findAll(pageable));
        return message;
    }

    @GetMapping("/{id}")
    public Message<ProductDto> getById(@PathVariable UUID id) {
        Message<ProductDto> message = new Message<>();

        productService.findById(id).ifPresentOrElse(
                dto -> {
                    message.setStatus(HttpStatus.OK);
                    message.setContent(dto);
                },
                () -> {
                    message.setStatus(HttpStatus.NOT_FOUND);
                    message.setContent(null);
                }
        );

        return message;
    }

    @PostMapping
    public Message<ProductDto> create(@RequestBody ProductDto dto) {
        Message<ProductDto> message = new Message<>();
        message.setStatus(HttpStatus.CREATED);
        message.setContent(productService.create(dto));
        return message;
    }

    @PutMapping("/{id}")
    public Message<ProductDto> update(
            @PathVariable UUID id,
            @RequestBody ProductDto dto
    ) {
        Message<ProductDto> message = new Message<>();

        productService.update(id, dto).ifPresentOrElse(
                updated -> {
                    message.setStatus(HttpStatus.OK);
                    message.setContent(updated);
                },
                () -> {
                    message.setStatus(HttpStatus.NOT_FOUND);
                    message.setContent(null);
                }
        );

        return message;
    }

    @DeleteMapping("/{id}")
    public Message<Void> delete(@PathVariable UUID id) {
        Message<Void> message = new Message<>();
        message.setStatus(HttpStatus.NO_CONTENT);
        message.setContent(null);
        productService.delete(id);
        return message;
    }
}
