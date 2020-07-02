package com.mamdy.soa;

import com.mamdy.entites.ProductInOrder;
import com.mamdy.entites.User;

public interface ProductInOrderService {
    void update(String itemId, Integer quantity, User user);

    ProductInOrder findOne(String itemId, User user);
}
