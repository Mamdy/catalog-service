package com.mamdy.dao;

import com.mamdy.entites.CommandeItem;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface CommandeItemRepository extends MongoRepository<CommandeItem, Long> {
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

