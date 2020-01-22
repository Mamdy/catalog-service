package com.mamdy.dao;

import com.mamdy.entites.Product;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
//import org.springframework.web.bind.annotation.CrossOrigin;
//CrossOrigin("*")
//@RepositoryRestResource(collectionResourceRel = "products", path = "products")
public interface ProductRepository extends MongoRepository<Product, String> {
    //Product saveProduct(@Param("product") Product product, String categoryId);
    //Product saveProduct(Product product,String categoryName);
}
