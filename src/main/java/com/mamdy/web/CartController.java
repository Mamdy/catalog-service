package com.mamdy.web;

import com.mamdy.dao.*;
import com.mamdy.entites.*;
import com.mamdy.form.ItemForm;
import com.mamdy.soa.CartService;
import com.mamdy.soa.ProductInOrderService;
import com.mamdy.soa.ProductService;
import com.mamdy.soa.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.swing.text.html.Option;
import java.security.Principal;
import java.util.*;

@CrossOrigin
@RestController
@RequestMapping("/cart")
@Slf4j
public class CartController {
    @Autowired
    CartService cartService;
    @Autowired
    UserService userService;
    @Autowired
    ProductService productService;
    @Autowired
    ProductInOrderService productInOrderService;
    @Autowired
    ProductInOrderRepository productInOrderRepository;

    @Autowired
    ClientRepository clientRepository;


    @Autowired
    ProductRepository productRepository;


    @Autowired
    CartRepository cartRepository;
    //public ResponseEntity<Cart> mergeCart(@RequestBody Collection<ProductInOrder> productInOrders, Client client, Principal principal)

    @PostMapping("")
    public ResponseEntity<Cart> mergeCart(@RequestBody ItemForm itemFormData, Principal principal) {
        Client customer = clientRepository.findByEmail(principal.getName());
        //si nouveau Client, on l'associe à un nouveau panier puis on l'enregistre en base
        if (customer == null) {
            customer = clientRepository.save(itemFormData.getClient());
            //on lui creer un nouveau panier
            Cart cart = new Cart();
            cart.setProductsInOrder(new HashSet<>());
            cart.setClient(customer);
            cart = cartRepository.save(cart);
            customer.setCart(cart);
            customer = clientRepository.save(customer);
        }

        try {
            cartService.mergeLocalCart(itemFormData.getLocalCartProductsInOrder(), customer);
        } catch (Exception e) {
            ResponseEntity.badRequest().body("Merge Cart Failed");
        }

        return ResponseEntity.ok(cartService.getCart(customer));
    }

    @GetMapping("")
    public Cart getCart(Principal principal) {
        Optional<Client> customer = Optional.ofNullable(clientRepository.findByEmail(principal.getName().toLowerCase()));
        if(customer.isPresent()) {
            final Cart cart = customer.get().getCart();
            return cart;
        }
        return null;
    }


    @PostMapping("/add")
    public boolean addToCart(@RequestBody ItemForm dataFromform, Principal principal) {
        Product productInfo;
        productInfo = productService.findByCode(dataFromform.getProductCode());
        try {
            ProductInOrder productInOrder = new ProductInOrder(productInfo, dataFromform.getQuantity());
            dataFromform.setLocalCartProductsInOrder(Collections.singleton(productInOrder));
            this.mergeCart(dataFromform, principal);
        } catch (Exception e) {
            log.info(e.getMessage().toString());
            return false;
        }
        return true;
    }

    @PutMapping("/{itemId}")
    public ProductInOrder modifyItem(@PathVariable("itemId") String itemId, @RequestBody Integer quantity, Principal principal) {
        Client client = clientRepository.findByEmail(principal.getName().toLowerCase());
        productInOrderService.update(itemId, quantity, client);
        return productInOrderService.findOne(itemId, client);
    }

    @DeleteMapping("/{itemId}")
    public void deleteItem(@PathVariable("itemId") String itemId, Principal principal) {
        cartService.delete(itemId, principal.getName());
    }

    @PostMapping("/checkout")
    public OrderMain checkout(Principal principal) {
        Client client = clientRepository.findByEmail(principal.getName().toLowerCase());
        return cartService.checkout(client);
    }


}
