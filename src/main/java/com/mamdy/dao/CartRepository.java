package com.mamdy.dao;

import com.mamdy.entites.Cart;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface CartRepository extends MongoRepository<Cart, String> {
//    List<Panier> findByEmail(String email);
//
//    Panier findByBufcartIdAndEmail(int bufcartId, String email);
//
//    void deleteByBufcartIdAndEmail(int bufcartId, String email);
//
//    List<Panier> findAllByEmail(String email);
//
//    List<Panier> findAllByOrderId(int orderId);
}

