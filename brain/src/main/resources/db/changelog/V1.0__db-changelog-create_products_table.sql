--liquibase formatted sql

--changeset dimitrov:1

CREATE TABLE products(
`id` INT PRIMARY KEY AUTO_INCREMENT,
`name` VARCHAR(255) NOT NULL,
`category` VARCHAR(255) NOT NULL,
`description` TEXT NOT NULL,
`quantity` INT NOT NULL,
`created_date` DATE NOT NULL,
`last_modified_date` DATE NOT NULL
) ENGINE=InnoDB AUTO_INCREMENT=1001;


# --rollback drop table products;
