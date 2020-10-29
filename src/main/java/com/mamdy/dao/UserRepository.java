package com.mamdy.dao;

import com.mamdy.entites.User;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Collection;


public interface UserRepository extends MongoRepository<User, String> {
    User findByEmail(String email);

    Collection<User> findAllByRole(String role);

}
