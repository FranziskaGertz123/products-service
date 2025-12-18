package com.example.products;

import com.example.products.api.ProductController;
import com.example.products.api.dto.IngredientDto;
import com.example.products.api.dto.ProductDto;
import com.example.products.application.ProductService;
import com.example.products.application.dao.Unit;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(ProductController.class)
class ProductControllerTest {

    @Autowired
    MockMvc mockMvc;

    @MockBean
    ProductService productService;

    @Test
    void getAll_returnsMessageWithPageContent() throws Exception {
        UUID id = UUID.randomUUID();
        var page = new PageImpl<>(List.of(new ProductDto(id, "Fries",  new BigDecimal("1.0"),1, List.of(new IngredientDto("Potato", new BigDecimal(200), Unit.GRAM, new BigDecimal(4))))));

        Mockito.when(productService.findAll(ArgumentMatchers.any(Pageable.class)))
                .thenReturn(page);

        mockMvc.perform(get("/api/products")
                        .param("page", "0")
                        .param("size", "20"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("OK"))
                .andExpect(jsonPath("$.content.content[0].id").value(id.toString()))
                .andExpect(jsonPath("$.content.content[0].name").value("Fries"));
    }

    @Test
    void getById_found_returnsMessageWithContent() throws Exception {
        UUID id = UUID.randomUUID();

        Mockito.when(productService.findById(id))
                .thenReturn(Optional.of(new ProductDto(id, "Bread",  new BigDecimal("1.0"),1, List.of(new IngredientDto("Flour", new BigDecimal(200), Unit.GRAM, new BigDecimal(4))))));

        mockMvc.perform(get("/api/products/{id}", id))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("OK"))
                .andExpect(jsonPath("$.content.id").value(id.toString()))
                .andExpect(jsonPath("$.content.name").value("Bread"));
    }

    @Test
    void getById_notFound_returnsMessageWith404AndNullContent() throws Exception {
        UUID id = UUID.randomUUID();

        Mockito.when(productService.findById(id))
                .thenReturn(Optional.empty());

        mockMvc.perform(get("/api/products/{id}", id))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("NOT_FOUND"))
                .andExpect(jsonPath("$.content", Matchers.nullValue()));
    }

    @Test
    void create_returnsCreatedMessage() throws Exception {
        UUID id = UUID.randomUUID();

        Mockito.when(productService.create(ArgumentMatchers.any(ProductDto.class)))
                .thenReturn(new ProductDto(id, "Pie",  new BigDecimal("1.0"),1, List.of(new IngredientDto("Potato", new BigDecimal(200), Unit.GRAM, new BigDecimal(4)))));

        mockMvc.perform(post("/api/products")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {"id":null,"name":"Apple Pie"}
                                """))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("CREATED"))
                .andExpect(jsonPath("$.content.id").value(id.toString()))
                .andExpect(jsonPath("$.content.name").value("Pie"));
    }

    @Test
    void update_notFound_returns404() throws Exception {
        UUID id = UUID.randomUUID();

        Mockito.when(productService.update(Mockito.eq(id), ArgumentMatchers.any(ProductDto.class)))
                .thenReturn(Optional.empty());

        mockMvc.perform(put("/api/products/{id}", id)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {"id":null,"name":"NewName"}
                                """))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("NOT_FOUND"))
                .andExpect(jsonPath("$.content", Matchers.nullValue()));
    }

    @Test
    void delete_returnsNoContentMessage() throws Exception {
        UUID id = UUID.randomUUID();

        mockMvc.perform(delete("/api/products/{id}", id))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("NO_CONTENT"))
                .andExpect(jsonPath("$.content", Matchers.nullValue()));

        Mockito.verify(productService).delete(id);
    }
}
