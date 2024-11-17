package com.dragi.finance_manager.cv;

public class CVNotFoundException extends RuntimeException {

    public CVNotFoundException(Long id) {
        super("Could not find CV " + id);
    }
}