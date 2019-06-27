package com.mamdy.dao;

import com.mamdy.entites.Product;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
//import org.springframework.web.bind.annotation.CrossOrigin;
//CrossOrigin("*")
@RepositoryRestResource
public interface ProductRepository extends MongoRepository<Product, String> {
}
