package com.mamdy.soa.impl;

import com.mamdy.dao.CartRepository;
import com.mamdy.dao.OrderRepository;
import com.mamdy.dao.ProductInOrderRepository;
import com.mamdy.entites.Cart;
import com.mamdy.entites.Client;
import com.mamdy.entites.OrderMain;
import com.mamdy.entites.ProductInOrder;
import com.mamdy.soa.CartService;
import com.mamdy.soa.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.Collection;
import java.util.HashSet;
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
        //on recupre le panier du client et on ajoute le/les nouveau produits à commander
        Cart finalCart = client.getCart();
        if (finalCart == null) {
            finalCart = new Cart();
            finalCart.setProductsInOrder(new HashSet<>());
            finalCart.setClient(client);
            finalCart = cartRepository.save(finalCart);
        }

        Cart finalCart1 = finalCart;
        productInOrders.forEach(productInOrder -> {

            Set<ProductInOrder> set = finalCart1.getProductsInOrder();
            Optional<ProductInOrder> old = set.stream().filter(e -> e.getProductCode().equals(productInOrder.getProductCode())).findFirst();
            ProductInOrder prod;
            if (old.isPresent()) {
                prod = old.get();
                prod.setCount(productInOrder.getCount() + prod.getCount());
            } else {
                prod = productInOrder;
                prod.setCart(finalCart1);
                finalCart1.getProductsInOrder().add(prod);
            }
            productInOrderRepository.save(prod);
        });

        cartRepository.save(finalCart1);

    }

    @Override
    @Transactional
    public void delete(String itemId, Client client) {
        Optional<ProductInOrder> productInOrder = client.getCart().getProductsInOrder().stream()
                .filter(e -> itemId.equals(e.getProductId()))
                .findFirst();

        productInOrder.ifPresent(productInOrder1 -> {
            productInOrder1.setCart(null);
            productInOrderRepository.deleteById(productInOrder1.getId());
        });

    }

    @Override
    @Transactional
    public void checkout(Client client) {

        OrderMain order = new OrderMain(client);
        order.setCreateTime(LocalDateTime.now());
        order.setProducts(client.getCart().getProductsInOrder());

        order = orderRepository.save(order);
        for (ProductInOrder productInOrder : client.getCart().getProductsInOrder()) {
            productInOrder.setCart(null);
            productInOrder.setOrderMain(order);
            productService.decreaseStock(productInOrder.getProductCode(), productInOrder.getCount());
            productInOrderRepository.save(productInOrder);

        }

    }
}
