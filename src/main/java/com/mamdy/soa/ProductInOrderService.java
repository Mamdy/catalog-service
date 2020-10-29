package com.mamdy.soa;

import com.mamdy.entites.Client;
import com.mamdy.entites.ProductInOrder;

public interface ProductInOrderService {
    void update(String itemId, Integer quantity, Client client);

    ProductInOrder findOne(String itemId, Client client);
}
