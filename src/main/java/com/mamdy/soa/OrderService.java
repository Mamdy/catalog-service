package com.mamdy.soa;

import com.mamdy.entites.OrderMain;
import com.mamdy.form.NewAdressForm;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface OrderService {
    Page<OrderMain> findAll(Pageable pageable);

    Page<OrderMain> findByStatus(Integer status, Pageable pageable);

    Page<OrderMain> findByBuyerEmail(String email, Pageable pageable);

    Page<OrderMain> findByBuyerPhone(String phone, Pageable pageable);

    OrderMain findOne(String orderId);

    OrderMain finish(String orderId);
    OrderMain update(OrderMain orderMain, NewAdressForm newAdressForm);

    OrderMain cancel(String orderId);


}
