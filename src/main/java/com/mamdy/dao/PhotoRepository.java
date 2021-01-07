package com.mamdy.dao;

import com.mamdy.entites.Cart;
import com.mamdy.entites.Photo;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface PhotoRepository extends MongoRepository<Photo, String> {
    Photo findByName(final String name);

}

