package com.mamdy.soa.impl;

import com.mamdy.dao.OrderRepository;
import com.mamdy.dao.ProductInOrderRepository;
import com.mamdy.dao.ProductRepository;
import com.mamdy.dao.UserRepository;
import com.mamdy.entites.OrderMain;
import com.mamdy.entites.Product;
import com.mamdy.entites.ProductInOrder;
import com.mamdy.enums.OrderStatusEnum;
import com.mamdy.enums.ResultEnum;
import com.mamdy.exception.MyException;
import com.mamdy.soa.OrderService;
import com.mamdy.soa.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public class OrderServiceImpl implements OrderService {
    @Autowired
    OrderRepository orderRepository;
    @Autowired
    UserRepository userRepository;
    @Autowired
    ProductRepository productRepository;
    @Autowired
    ProductService productService;
    @Autowired
    ProductInOrderRepository productInOrderRepository;

    @Override
    public Page<OrderMain> findAll(Pageable pageable) {
        return orderRepository.findAllByOrderByOrderStatusAscCreateTimeDesc(pageable);
    }

    @Override
    public Page<OrderMain> findByStatus(Integer status, Pageable pageable) {
        return orderRepository.findAllByOrderStatusOrderByCreateTimeDesc(status, pageable);
    }

    @Override
    public Page<OrderMain> findByBuyerEmail(String email, Pageable pageable) {
        return orderRepository.findAllByBuyerEmailOrderByOrderStatusAscCreateTimeDesc(email, pageable);

    }

    @Override
    public Page<OrderMain> findByBuyerPhone(String phone, Pageable pageable) {
        return orderRepository.findAllByBuyerPhoneOrderByOrderStatusAscCreateTimeDesc(phone, pageable);
    }

    @Override
    public OrderMain findOne(String orderId) {
        OrderMain orderMain = orderRepository.findById(orderId);
        if (orderMain == null) {
            throw new MyException(ResultEnum.ORDER_NOT_FOUND);
        }
        return orderMain;
    }

    @Override
    public OrderMain finish(String orderId) {
        OrderMain orderMain = findOne(orderId);
        if (!orderMain.getOrderStatus().equals(OrderStatusEnum.NEW.getCode())) {
            throw new MyException(ResultEnum.ORDER_STATUS_ERROR);
        }

        orderMain.setOrderStatus(OrderStatusEnum.FINISHED.getCode());
        orderRepository.save(orderMain);
        return orderRepository.findById(orderId);
    }


    @Override
    public OrderMain cancel(String orderId) {
        OrderMain orderMain = findOne(orderId);
        if (!orderMain.getOrderStatus().equals(OrderStatusEnum.NEW.getCode())) {
            throw new MyException(ResultEnum.ORDER_STATUS_ERROR);
        }

        orderMain.setOrderStatus(OrderStatusEnum.CANCELED.getCode());
        orderRepository.save(orderMain);

        // Restore Stock
        Iterable<ProductInOrder> products = orderMain.getProducts();
        for (ProductInOrder productInOrder : products) {
            Product productInfo = productRepository.findByCode(productInOrder.getProductCode());
            if (productInfo != null) {
                productService.increaseStock(productInOrder.getId(), productInOrder.getCount());
            }
        }
        return orderRepository.findById(orderId);
    }
}
