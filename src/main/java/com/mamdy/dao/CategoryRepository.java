package com.mamdy.dao;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import com.mamdy.entites.Category;

@RepositoryRestResource
public interface CategoryRepository extends MongoRepository<Category, String> {
	public static final Logger logger = LoggerFactory.getLogger(CategoryRepository.class);

	@Query("{ 'name' : ?0 }")
	Category findByName(String name);

}
