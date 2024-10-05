package com.dragi.finance_manager.transaction;

public class TransactionNotFoundException extends RuntimeException {

    TransactionNotFoundException(Long id) {
        super("Could not find transaction " + id);
    }
}
