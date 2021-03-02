package com.mamdy.soa.impl;

import com.mamdy.dao.CartRepository;
import com.mamdy.dao.ClientRepository;
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
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Collectors;

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

    @Autowired
    ClientRepository clientRepository;


    @Override
    public Cart getCart(Client client) {
        return client.getCart();
    }

    @Override
    @Transactional
    public void mergeLocalCart(Collection<ProductInOrder> productInOrders, Client customer) {
        //on recupère le panier du client et on y ajoute le/les nouveaux produits à commander
        Optional<Cart> finalCart = Optional.ofNullable(customer.getCart());
        finalCart.ifPresent(fc->{
            productInOrders.forEach(productInOrder -> {
                Set<ProductInOrder> set = fc.getProductsInOrder();
                Optional<ProductInOrder> old = set.stream().filter(e -> e.getProductCode().
                        equals(productInOrder.getProductCode())).findFirst();
                ProductInOrder prod;
                if (old.isPresent()) {
                    prod = old.get();
                    prod.setCount(productInOrder.getCount() + prod.getCount());
                } else {
                    prod = productInOrder;
                    prod.setCart(fc);
                    fc.getProductsInOrder().add(prod);
                }
                productInOrderRepository.save(prod);
            });
            cartRepository.save(fc);
        });


    }

    @Override
    @Transactional
    public void delete(String itemId, String customerEmail) {
        Client client = clientRepository.findByEmail(customerEmail);
        Optional<ProductInOrder> productInOrder = client.getCart().getProductsInOrder().stream()
                .filter(e -> itemId.equals(e.getProductCode()))
                .findFirst();

        productInOrder.ifPresent(productInOrder1 -> {
            productInOrder1.setCart(null);
            //productInOrderRepository.deleteById(productInOrder1.getId());
            //il faut supprimer sa reference dans son panier
            Cart finalCart = client.getCart();
            finalCart.getProductsInOrder().remove(productInOrder1);
            cartRepository.save(finalCart);
            productInOrderRepository.save(productInOrder1);

        });

    }

    @Override
    @Transactional
    public OrderMain checkout(Client client) {
        final LocalDateTime ldt = LocalDateTime.now();
        String numOrder = this.generateNumOrder(ldt);
        String shippingAdress=client.getFirstName() +
                " " + client.getLastName() + ", "+
                client.getAddress()+", "+
                client.getCodePostal()+" "+
                client.getVille()+","+
                client.getPays();

        OrderMain order = new OrderMain(client);
        order.setCreateTime(LocalDateTime.now());
        order.setProducts(client.getCart().getProductsInOrder());
        order.setNumOrder(numOrder.toUpperCase());
        order.setShippingAddress(shippingAdress);
        return orderRepository.save(order);

    }

    private String generateNumOrder(LocalDateTime ldt){
        final String date = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm", Locale.FRANCE).format(ldt);
        return RandomString.make(3) + date.substring(0,4) + date.substring(5,7) + date.substring(8,10)+ date.substring(11,13)+ date.substring(14,16);

    }

}
