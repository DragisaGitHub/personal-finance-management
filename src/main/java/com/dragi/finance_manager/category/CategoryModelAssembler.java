package com.dragi.finance_manager.category;

import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Component
public class CategoryModelAssembler implements RepresentationModelAssembler<Category, EntityModel<Category>> {

    @Override
    public EntityModel<Category> toModel(Category category) {

        return EntityModel.of(category, //
                linkTo(methodOn(CategoryController.class).getCategoryById(category.getId())).withSelfRel(),
                linkTo(methodOn(CategoryController.class).getAllCategories()).withRel("categories"));
    }
}
