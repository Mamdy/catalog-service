package com.mamdy.web;

import com.mamdy.entites.OrderMain;
import com.mamdy.entites.ProductInOrder;
import com.mamdy.form.NewAdressForm;
import com.mamdy.form.OrderForm;
import com.mamdy.soa.OrderService;
import com.mamdy.soa.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.web.bind.annotation.*;

import java.util.Collection;

@RestController
@CrossOrigin
public class OrderController {
    @Autowired
    OrderService orderService;
    @Autowired
    UserService userService;

    @GetMapping("/order")
    public Page<OrderMain> orderList(@RequestParam(value = "page", defaultValue = "1") Integer page,
                                     @RequestParam(value = "size", defaultValue = "10") Integer size,
                                     Authentication authentication) {
        PageRequest request = PageRequest.of(page - 1, size);
        Page<OrderMain> orderPage;
        if (authentication.getAuthorities().contains(new SimpleGrantedAuthority("CUSTOMER"))) {
            orderPage = orderService.findByBuyerEmail(authentication.getName(), request);
        } else {
            orderPage = orderService.findAll(request);
        }
        return orderPage;
    }


    @PatchMapping("/order/cancel/{id}")
    public ResponseEntity<OrderMain> cancel(@PathVariable("id") String orderId, Authentication authentication) {
        OrderMain orderMain = orderService.findOne(orderId);
        if (!authentication.getName().equals(orderMain.getBuyerEmail()) && authentication.getAuthorities().contains(new SimpleGrantedAuthority("CUSTOMER"))) {

            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        return ResponseEntity.ok(orderService.cancel(orderId));
    }

    @PatchMapping("/order/update/{id}")
    public ResponseEntity<OrderMain> modifyShippingAdresse(@PathVariable("id") String orderId,
                                                           @RequestBody NewAdressForm newAdressForm,
                                                           Authentication authentication) {
        OrderMain orderMain = orderService.findOne(orderId);
        if (!authentication.getName().equals(orderMain.getBuyerEmail()) && authentication.getAuthorities().contains(new SimpleGrantedAuthority("CUSTOMER"))) {

            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        return ResponseEntity.ok(orderService.update(orderMain,newAdressForm));
    }

    @PatchMapping("/order/finish/{id}")
    public ResponseEntity<OrderMain> finish(@PathVariable("id") String orderId, Authentication authentication) {
        if (authentication.getAuthorities().contains(new SimpleGrantedAuthority("CUSTOMER"))) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        return ResponseEntity.ok(orderService.finish(orderId));
    }


    @GetMapping("/order/{id}")
    public ResponseEntity show(@PathVariable("id") String orderId, Authentication authentication) {
        boolean isCustomer = authentication.getAuthorities().contains(new SimpleGrantedAuthority("CUSTOMER"));
        OrderMain orderMain = orderService.findOne(orderId);
        if (isCustomer && !authentication.getName().equals(orderMain.getBuyerEmail())) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        Collection<ProductInOrder> items = orderMain.getProducts();
        return ResponseEntity.ok(orderMain);
    }
}
