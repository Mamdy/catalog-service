package com.mamdy.dao;

import com.mamdy.entites.Client;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface ClientRepository extends MongoRepository<Client, String> {
    Client findByEmail(String email);
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

