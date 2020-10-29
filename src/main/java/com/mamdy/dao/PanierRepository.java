package com.mamdy.dao;

import com.mamdy.entites.Panier;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface PanierRepository extends MongoRepository<Panier, String> {
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

