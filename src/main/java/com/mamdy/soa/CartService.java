package com.mamdy.soa;

import com.mamdy.entites.Cart;
import com.mamdy.entites.Client;
import com.mamdy.entites.ProductInOrder;
import com.mamdy.entites.User;

import java.util.Collection;

public interface CartService {
    Cart getCart(Client client);

    void mergeLocalCart(Collection<ProductInOrder> productInOrders, Client client);

    void delete(String itemId, User user);

    void checkout(User user);

}
