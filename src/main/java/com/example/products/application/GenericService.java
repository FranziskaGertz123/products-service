package com.example.products.application;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

public interface GenericService<DTO, ID> {

    Page<DTO> findAll(Pageable pageable);

    Optional<DTO> findById(ID id);

    DTO create(DTO dto);

    Optional<DTO> update(ID id, DTO dto);

    void delete(ID id);
}