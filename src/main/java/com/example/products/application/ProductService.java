package com.example.products.application;

import com.example.products.messaging.events.ProductCreatedEvent;
import com.example.products.messaging.events.ProductDeletedEvent;
import com.example.products.messaging.ProductEventPublisher;
import com.example.products.messaging.events.ProductUpdatedEvent;
import com.example.products.application.dao.Ingredient;
import com.example.products.application.dao.Product;
import com.example.products.api.dto.IngredientDto;
import com.example.products.api.dto.ProductDto;
import java.util.UUID;
import com.example.products.application.repository.ProductRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class ProductService implements GenericService<ProductDto, UUID> {

    private final ProductRepository productRepository;
    private final ProductEventPublisher publisher;

    public ProductService(ProductRepository productRepository, ProductEventPublisher publisher) {
        this.productRepository = productRepository;
        this.publisher = publisher;
    }

    @Override
    public Page<ProductDto> findAll(Pageable pageable) {
        return productRepository
                .findAll(pageable)
                .map(this::toDto);
    }

    @Override
    public Optional<ProductDto> findById(UUID id) {
        return productRepository
                .findById(id)
                .map(this::toDto);
    }

    @Override
    public ProductDto create(ProductDto dto) {
        Product product = new Product();
        product.setName(dto.name());

        Product saved = productRepository.save(product);

        publisher.publishProductCreated(
                new ProductCreatedEvent(saved.getId(), saved.getName(), dto.ingredients())
        );

        return toDto(saved);

    }

    @Override
    public Optional<ProductDto> update(UUID id, ProductDto dto) {
        return productRepository.findById(id)
                .map(existing -> {
                    existing.setName(dto.name());
                    Product saved = productRepository.save(existing);

                    publisher.publishProductUpdated(
                            new ProductUpdatedEvent(saved.getId(), saved.getName(), dto.ingredients())
                    );

                    return toDto(saved);

                });
    }

    @Override
    public void delete(UUID id) {
        productRepository.deleteById(id);

        publisher.publishProductDeleted(
                new ProductDeletedEvent(id)
        );

    }


    private IngredientDto toDto(Ingredient i) {
        return new IngredientDto(
                i.getName(),
                i.getAmount(),
                i.getUnit()
        );
    }

    private ProductDto toDto(Product product) {
        return new ProductDto(
                product.getId(),
                product.getName(),
                product.getPrice(),
                product.getIngredients().stream()
                        .map(this::toDto)
                        .toList()
        );
    }

}
