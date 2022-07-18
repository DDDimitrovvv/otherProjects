--liquibase formatted sql

--changeset dimitrov:1

CREATE TABLE baseid
(
    next_val      BIGINT PRIMARY KEY AUTO_INCREMENT,
    sequence_name VARCHAR(255) NOT NULL
) engine = InnoDB;

INSERT INTO baseid(sequence_name, next_val)
VALUES ('products', 1000);

CREATE TABLE products
(
    `id`                 BIGINT PRIMARY KEY AUTO_INCREMENT,
    `name`               VARCHAR(255) NOT NULL,
    `category`           VARCHAR(255) NOT NULL,
    `description`        TEXT         NOT NULL,
    `quantity`           INT          NOT NULL,
    `created_date`       DATE         NOT NULL,
    `last_modified_date` DATE         NOT NULL
) ENGINE = InnoDB
  AUTO_INCREMENT = 1001;


# --rollback drop table products
