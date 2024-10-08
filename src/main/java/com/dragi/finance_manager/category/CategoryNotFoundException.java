package com.dragi.finance_manager.category;

public class CategoryNotFoundException extends RuntimeException {

    CategoryNotFoundException(Long id) {
        super("Could not find category " + id);
    }
}
