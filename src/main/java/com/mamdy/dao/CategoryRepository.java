package com.mamdy.dao;

import com.mamdy.entites.Category;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.stereotype.Repository;

@RepositoryRestResource
public interface CategoryRepository extends MongoRepository<Category,String> {
    public static final Logger logger = LoggerFactory.getLogger(CategoryRepository.class);

}
