package com.example.products.application;

import com.example.products.application.repository.ProductRepository;
import com.example.products.messaging.events.ProductCreatedEvent;
import com.example.products.messaging.events.ProductDeletedEvent;
import com.example.products.messaging.ProductEventPublisher;
import com.example.products.messaging.events.ProductUpdatedEvent;
import com.example.products.application.dao.Ingredient;
import com.example.products.application.dao.Product;
import com.example.products.api.dto.IngredientDto;
import com.example.products.api.dto.ProductDto;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public class ProductService implements GenericService<ProductDto, UUID> {

    private final ProductRepository productRepository;
    private final ProductEventPublisher eventPublisher;

    public ProductService(ProductRepository productRepository, ProductEventPublisher eventPublisher) {
        this.productRepository = productRepository;
        this.eventPublisher = eventPublisher;
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
        product.setRecipeVersion(1);

        product.setIngredients(toEntityIngredients(dto.ingredients()));

        Product saved = productRepository.save(product);

        UUID eventId = UUID.randomUUID();
        Instant occurredAt = Instant.now();

        eventPublisher.publishProductCreated(
                new ProductCreatedEvent(
                        eventId,
                        occurredAt,
                        saved.getId(),
                        saved.getName(),
                        saved.getRecipeVersion(),
                        dto.ingredients()
                )
        );

        return toDto(saved);
    }

    @Override
    public Optional<ProductDto> update(UUID id, ProductDto dto) {
        return productRepository.findById(id)
                .map(existing -> {
                    existing.setName(dto.name());

                    existing.getIngredients().clear();
                    existing.getIngredients().addAll(toEntityIngredients(dto.ingredients()));

                    existing.setRecipeVersion(existing.getRecipeVersion() + 1);

                    Product saved = productRepository.save(existing);

                    UUID eventId = UUID.randomUUID();
                    Instant occurredAt = Instant.now();

                    eventPublisher.publishProductUpdated(
                            new ProductUpdatedEvent(
                                    eventId,
                                    occurredAt,
                                    saved.getId(),
                                    saved.getName(),
                                    saved.getRecipeVersion(),
                                    dto.ingredients()
                            )
                    );

                    return toDto(saved);
                });
    }

    @Override
    public void delete(UUID id) {
        productRepository.deleteById(id);
        UUID eventId = UUID.randomUUID();
        Instant occurredAt = Instant.now();

        eventPublisher.publishProductDeleted(
                new ProductDeletedEvent(
                        eventId,
                        occurredAt,
                        id
                )
        );
    }

    private ProductDto toDto(Product product) {
        return new ProductDto(
                product.getId(),
                product.getName(),
                product.getPrice(),
                product.getRecipeVersion(),
                toDtoIngredients(product.getIngredients())
        );
    }

    private List<Ingredient> toEntityIngredients(List<IngredientDto> dtos) {
        if (dtos == null) return new ArrayList<>();

        List<Ingredient> result = new ArrayList<>();
        for (IngredientDto dto : dtos) {
            if (dto == null) continue;

            Ingredient i = new Ingredient();
            i.setName(dto.name());

            i.setUsage(dto.usage() == null ? BigDecimal.ZERO : dto.usage());

            i.setUnit(dto.unit() == null ? null : dto.unit());

            i.setPrice(dto.price() == null ? BigDecimal.ZERO : dto.price());

            result.add(i);
        }
        return result;
    }

    private List<IngredientDto> toDtoIngredients(List<Ingredient> entities) {
        if (entities == null) return List.of();

        return entities.stream()
                .map(e -> new IngredientDto(
                        e.getName(),
                        e.getUsage(),
                        e.getUnit() == null ? null : e.getUnit(),
                        e.getPrice()
                ))
                .toList();
    }
}
