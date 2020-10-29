package com.mamdy.dao;

import com.mamdy.entites.Category;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

public interface CategoryRepository extends MongoRepository<Category, String> {

    @Query("{ 'name' : ?0 }")
    Category findByName(String name);


}
