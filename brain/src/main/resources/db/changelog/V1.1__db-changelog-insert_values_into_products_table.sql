--liquibase formatted sql

--changeset dimitrov:2

INSERT INTO brain_db.products (`name`, `category`, `description`, `quantity`, `created_date`, `last_modified_date`)
VALUES ('Dell 5401', 'Laptop', 'Dell description', 12, '2020-05-20', '2020-06-30'),
('Dell U2413', 'Monitor', 'Dell monitor', 15, '2020-05-20', '2020-06-30'),
('Samsung', 'Monitor', 'Samsung monitor', 9, '2020-06-30', '2020-07-15');

