package com.mamdy.soa.impl;

import antlr.StringUtils;
import com.mamdy.dao.CartRepository;
import com.mamdy.dao.OrderRepository;
import com.mamdy.dao.ProductInOrderRepository;
import com.mamdy.entites.Cart;
import com.mamdy.entites.Client;
import com.mamdy.entites.OrderMain;
import com.mamdy.entites.ProductInOrder;
import com.mamdy.soa.CartService;
import com.mamdy.soa.ProductService;
import net.bytebuddy.utility.RandomString;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

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
        //on recupre le panier du client et on y ajoute le/les nouveaux produits à commander
        Cart finalCart = client.getCart();
        if (finalCart == null) {
            finalCart = new Cart();
            finalCart.setProductsInOrder(new HashSet<>());
            finalCart.setClient(client);
            finalCart = cartRepository.save(finalCart);
        }else if (finalCart.getProductsInOrder().size() > 0 && finalCart.getProductsInOrder().contains(null)){
            Cart finalCart1 = finalCart;
            productInOrders.forEach(productInOrder -> {

                ProductInOrder prod = productInOrder;
                    prod.setCart(finalCart1);
                    finalCart1.getProductsInOrder().add(prod);

                productInOrderRepository.save(prod);
            });
            cartRepository.save(finalCart1);

        }

        Cart finalCart1 = finalCart;
            productInOrders.forEach(productInOrder -> {
                Set<ProductInOrder> set = finalCart1.getProductsInOrder();

                Optional<ProductInOrder> old = set.stream().filter(e -> e.getProductCode().
                        equals(productInOrder.getProductCode())).findFirst();
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
                .filter(e -> itemId.equals(e.getProductCode()))
                .findFirst();

        productInOrder.ifPresent(productInOrder1 -> {
            productInOrder1.setCart(null);
            productInOrderRepository.deleteById(productInOrder1.getId());
        });

    }

    @Override
    @Transactional
    public void checkout(Client client) {
        final LocalDateTime ldt = LocalDateTime.now();

        final String date = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm", Locale.FRANCE).format(ldt);
        String numOrder = RandomString.make(3) + date.substring(2,3) + client.getFirstName().substring(0,1) + date.substring(5,6) + date.substring(8,9);
        System.out.println(numOrder);


        OrderMain order = new OrderMain(client);
        order.setCreateTime(LocalDateTime.now());
        order.setProducts(client.getCart().getProductsInOrder());
        order.setNumOrder(numOrder);

        order = orderRepository.save(order);
        for (ProductInOrder productInOrder : client.getCart().getProductsInOrder()) {
            productInOrder.setCart(null);
            productInOrder.setOrderMain(order);
            productService.decreaseStock(productInOrder.getProductCode(), productInOrder.getCount());
            productInOrderRepository.save(productInOrder);

        }

    }

    private String generateOrderNum(String num){
        return num + RandomString.make(5);
    }
}
