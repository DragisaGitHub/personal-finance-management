package com.dragi.finance_manager.category;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.hateoas.EntityModel;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.Collections;

import static org.mockito.Mockito.*;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

class CategoryControllerTest {

    @Mock
    private CategoryService categoryService;

    @Mock
    private CategoryModelAssembler categoryModelAssembler;

    @InjectMocks
    private CategoryController categoryController;

    private MockMvc mockMvc;
    private Category category;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(categoryController).build();

        category = new Category();
        category.setId(1L);
        category.setName("Groceries");
    }

    @Test
    void shouldCreateCategory() throws Exception {
        EntityModel<Category> categoryEntityModel = EntityModel.of(category,
                linkTo(methodOn(CategoryController.class).getCategoryById(category.getId())).withSelfRel());

        when(categoryService.createCategory(any(Category.class))).thenReturn(category);
        when(categoryModelAssembler.toModel(any(Category.class))).thenReturn(categoryEntityModel);

        mockMvc.perform(post("/api/categories")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"name\": \"Groceries\"}"))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.name").value("Groceries"));

        verify(categoryService, times(1)).createCategory(any(Category.class));
    }


    @Test
    void shouldGetAllCategories() throws Exception {
        EntityModel<Category> categoryEntityModel = EntityModel.of(category,
                linkTo(methodOn(CategoryController.class).getCategoryById(category.getId())).withSelfRel());

        when(categoryService.getAllCategories()).thenReturn(Collections.singletonList(category));
        when(categoryModelAssembler.toModel(any(Category.class))).thenReturn(categoryEntityModel);

        mockMvc.perform(get("/api/categories"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content[0].name").value("Groceries"));

        verify(categoryService, times(1)).getAllCategories();
    }

}