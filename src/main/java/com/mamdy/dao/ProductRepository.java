package com.mamdy.dao;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import com.mamdy.entites.Product;
//import org.springframework.web.bind.annotation.CrossOrigin;
//CrossOrigin("*")
//@RepositoryRestResource(collectionResourceRel = "products", path = "products")
@RepositoryRestResource
public interface ProductRepository extends MongoRepository<Product, String> {
    //Product saveProduct(@Param("product") Product product, String categoryId);
    //Product saveProduct(Product product,String categoryName);
}
