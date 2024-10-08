package com.dragi.finance_manager.category;

import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.IanaLinkRelations;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@RestController
@RequestMapping("/api/categories")
public class CategoryController {

    private final CategoryService categoryService;
    private final CategoryModelAssembler categoryModelAssembler;

    public CategoryController(CategoryService categoryService, CategoryModelAssembler categoryModelAssembler) {
        this.categoryService = categoryService;
        this.categoryModelAssembler = categoryModelAssembler;
    }

    @GetMapping
    public CollectionModel<EntityModel<Category>> getAllCategories() {

        List<EntityModel<Category>> categories = categoryService.getAllCategories().stream()
                .map(categoryModelAssembler::toModel)
                .collect(Collectors.toList());

        return CollectionModel.of(categories, linkTo(methodOn(CategoryController.class).getAllCategories()).withSelfRel());
    }

    @GetMapping("/{id}")
    public EntityModel<Category> getCategoryById(@PathVariable Long id) {
        Category category = categoryService.getCategoryById(id).orElseThrow(() -> new CategoryNotFoundException(id));
        return categoryModelAssembler.toModel(category);
    }

    @PostMapping
    public ResponseEntity<?> createCategory(@RequestBody Category category) {
        EntityModel<Category> categoryModel = categoryModelAssembler.toModel(categoryService.createCategory(category));
        return ResponseEntity
                .created(categoryModel.getRequiredLink(IanaLinkRelations.SELF).toUri())
                .body(categoryModel);
    }

    @PutMapping("/{id}")
    ResponseEntity<?> replaceCategory(@RequestBody Category newCategory, @PathVariable Long id) {

        Category updatedCategory = categoryService.getCategoryById(id)
                .map(category -> {
                    category.setName(newCategory.getName());
                    return categoryService.createCategory(category);
                })
                .orElseGet(() -> {
                    newCategory.setId(id);
                    return categoryService.createCategory(newCategory);
                });

        EntityModel<Category> entityModel = categoryModelAssembler.toModel(updatedCategory);

        return ResponseEntity
                .created(entityModel.getRequiredLink(IanaLinkRelations.SELF).toUri())
                .body(entityModel);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCategory(@PathVariable Long id) {
        categoryService.deleteCategory(id);
        return ResponseEntity.noContent().build();
    }
}