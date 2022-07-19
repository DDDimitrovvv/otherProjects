#brain project

This project is REST API based on Spring Boot. The project contains data for technology products (notebooks, monitors, etc.).

The main path "/products" show all available products.

### Used technologies and architectures:
1. SpringBoot;
2. MySQL database;
3. Liquibase
4. REST services;
5. SpringBoot tests with Mockito;

###There are multiple paths specified for this project:
- "/products/{id}" - visualized product by id;
- "/products/add" - adding a product in the database;
- "/products/update/{id}" - updating an existing product in the database;
- "/products/{id}/order/{quantity}" - ordering product by given id and quantity;
- "/products/categories" - showing all available categories with corresponding quantity;
- "/products/delete/{id}" - delete a product from database;
- "/products/delete/all" - delete all products from database;
- "/products?orderBy={variable}&direction={variable}&page={variable}&pageSize={variable}" - visualize list of products with page and numbers of products regarding the specific user request (sorted by category parameter, ordered asc/desc on a specific page);


