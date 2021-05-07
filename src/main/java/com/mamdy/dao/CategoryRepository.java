package com.mamdy.dao;

import com.mamdy.entites.Category;
import com.mamdy.entites.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import java.util.Optional;

public interface CategoryRepository extends MongoRepository<Category, String> {

    @Query("{ 'name' : ?0 }")
    Category findByName(String name);
    Category findByNameContaining(String name);
    Category findByNameLike(String likeName);
    Category findByNameStartingWith(String name);
    Optional<Category>  findById(final Long id);




}
