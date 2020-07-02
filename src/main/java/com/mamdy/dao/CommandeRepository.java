package com.mamdy.dao;

import com.mamdy.entites.Commande;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface CommandeRepository extends MongoRepository<Commande, Long> {

//    OrderMain findByOrderId(Long orderId);
//
//    Page<OrderMain> findAllByOrderStatusOrderByCreateTimeDesc(Integer orderStatus, Pageable pageable);
//
//    Page<OrderMain> findAllByBuyerEmailOrderByOrderStatusAscCreateTimeDesc(String buyerEmail, Pageable pageable);
//
//    Page<OrderMain> findAllByOrderByOrderStatusAscCreateTimeDesc(Pageable pageable);
//
//    Page<OrderMain> findAllByBuyerPhoneOrderByOrderStatusAscCreateTimeDesc(String buyerPhone, Pageable pageable);

}

