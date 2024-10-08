CREATE SCHEMA IF NOT EXISTS finance_schema;

CREATE TABLE IF NOT EXISTS finance_schema.category (
                                                       id BIGINT GENERATED BY DEFAULT AS IDENTITY,
                                                       name VARCHAR(255),
    PRIMARY KEY (id)
    );

CREATE TABLE IF NOT EXISTS finance_schema.transaction (
                                                          id BIGINT GENERATED BY DEFAULT AS IDENTITY,
                                                          amount FLOAT(53) NOT NULL,
    date DATE,
    description VARCHAR(255),
    type VARCHAR(255) CHECK (type IN ('INCOME', 'EXPENSE')),
    username VARCHAR(255),
    category_id BIGINT NOT NULL,
    PRIMARY KEY (id),
    CONSTRAINT fk_category FOREIGN KEY (category_id) REFERENCES finance_schema.category(id)
    );