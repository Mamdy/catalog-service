package com.mamdy.soa.impl;

import com.mamdy.dao.ProductInOrderRepository;
import com.mamdy.entites.Client;
import com.mamdy.entites.ProductInOrder;
import com.mamdy.soa.ProductInOrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;
import java.util.concurrent.atomic.AtomicReference;


@Service
public class ProductInOrderServiceImpl implements ProductInOrderService {

    @Autowired
    ProductInOrderRepository productInOrderRepository;

    @Override
    @Transactional
    public void update(String itemId, Integer quantity, Client client) {
        Optional<ProductInOrder> op;
        op = client.getCart().getProductsInOrder().stream().filter(e -> itemId.equals(e.getProductId())).findFirst();
        if (op.isPresent()) {
            ProductInOrder productInOrder = op.get();
            productInOrder.setCount(quantity);
            productInOrderRepository.save(productInOrder);

        }

//        op.ifPresent(productInOrder -> {
//            productInOrder.setCount(quantity);
//            productInOrderRepository.save(productInOrder);
//        });

    }

    @Override
    public ProductInOrder findOne(String itemId, Client client) {
        Optional<ProductInOrder> op;
        op = client.getCart().getProductsInOrder().stream().filter(e -> itemId.equals(e.getProductId())).findFirst();
        AtomicReference<ProductInOrder> res = new AtomicReference<>();
        op.ifPresent(res::set);
        return res.get();
    }
}
