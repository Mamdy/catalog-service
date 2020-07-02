package com.mamdy.soa.impl;

import com.mamdy.dao.CartRepository;
import com.mamdy.dao.OrderRepository;
import com.mamdy.dao.ProductInOrderRepository;
import com.mamdy.entites.*;
import com.mamdy.soa.CartService;
import com.mamdy.soa.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.Collection;
import java.util.Optional;
import java.util.Set;

@Service
public class CartServiceImpl implements CartService {
    @Autowired
    ProductService productService;

    @Autowired
    OrderRepository orderRepository;

    @Autowired
    ProductInOrderRepository productInOrderRepository;

    @Autowired
    CartRepository cartRepository;


    @Override
    public Cart getCart(Client client) {
        return client.getCart();
    }

    @Override
    @Transactional
    public void mergeLocalCart(Collection<ProductInOrder> productInOrders, Client client) {
        Cart finalCart = client.getCart();
        productInOrders.forEach(productInOrder -> {
            Set<ProductInOrder> set = finalCart.getProductsInOrder();
            Optional<ProductInOrder> old = set.stream().filter(e -> e.getProductId().equals(productInOrder.getProductId())).findFirst();
            ProductInOrder prod;
            if (old.isPresent()) {
                prod = old.get();
                prod.setCount(productInOrder.getCount() + prod.getCount());
            } else {
                prod = productInOrder;
                prod.setCart(finalCart);
                finalCart.getProductsInOrder().add(prod);
            }
            productInOrderRepository.save(prod);
        });

        cartRepository.save(finalCart);


    }

    @Override
    @Transactional
    public void delete(String itemId, User user) {
        Optional<ProductInOrder> productInOrder = user.getCart().getProductsInOrder().stream()
                .filter(e -> itemId.equals(e.getProductId()))
                .findFirst();

        productInOrder.ifPresent(productInOrder1 -> {
            productInOrder1.setCart(null);
            productInOrderRepository.deleteById(productInOrder1.getId());
        });

    }

    @Override
    @Transactional
    public void checkout(User user) {
        // Creation d'une commande
        OrderMain orderMain = new OrderMain(user);


    }
}
