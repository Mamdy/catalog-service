package com.mamdy.web;

import com.mamdy.dao.*;
import com.mamdy.entites.*;
import com.mamdy.form.ItemForm;
import com.mamdy.form.OrderForm;
import com.mamdy.form.OrderProduct;
import com.mamdy.soa.CartService;
import com.mamdy.soa.ProductInOrderService;
import com.mamdy.soa.ProductService;
import com.mamdy.soa.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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
    CommandeRepository commandeRepository;

    @Autowired
    ProductRepository productRepository;

    @Autowired
    CommandeItemRepository commandeItemRepository;

    @Autowired
    CartRepository cartRepository;
    //public ResponseEntity<Cart> mergeCart(@RequestBody Collection<ProductInOrder> productInOrders, Client client, Principal principal)

    @PostMapping("")
    public ResponseEntity<Cart> mergeCart(@RequestBody ItemForm itemFormData, Principal principal) {
        //si authentification est reussi on genere le token
        // User user1 = new User(userDetails.getUsername(),userDetails.getPassword());
        //        Client newCustomer = clientRepository.findByUsername(principal.getName().toLowerCase());

        Client newCustomer = clientRepository.findByUsername(itemFormData.getClient().getUsername());
        if (newCustomer == null) {
            newCustomer = clientRepository.save(itemFormData.getClient());
            if (newCustomer.getEmail().equalsIgnoreCase(principal.getName()) && newCustomer.getCart() == null) {

                Cart cart = new Cart();
                cart.setProductsInOrder(new HashSet<>());
                cart.setClient(newCustomer);
                cart = cartRepository.save(cart);

                newCustomer.setCart(cart);
                clientRepository.save(newCustomer);

            }
        }



        try {
            cartService.mergeLocalCart(itemFormData.getLocalCartProductsInOrder(), newCustomer);
        } catch (Exception e) {
            ResponseEntity.badRequest().body("Merge Cart Failed");
        }
        return ResponseEntity.ok(cartService.getCart(newCustomer));
    }

//    private Cart mergCart(Collection<ProductInOrder> productInOrders, Client client, Principal principal){
//        Client newCustomer = clientRepository.findByUsername(client.getUsername());
//        if (newCustomer == null) {
//            newCustomer = clientRepository.save(client);
//            if (newCustomer.getEmail().equalsIgnoreCase(principal.getName()) && newCustomer.getCart() == null) {
//
//                Cart cart = new Cart();
//                cart.setProductsInOrder(new HashSet<>());
//                cart.setClient(newCustomer);
//                cart = cartRepository.save(cart);
//
//                newCustomer.setCart(cart);
//                clientRepository.save(newCustomer);
//
//            }
//        }
//
//        cartService.mergeLocalCart(productInOrders, newCustomer);
//
//
//        return  cartService.getCart(newCustomer);
//    }
    @GetMapping("")
    public Set<ProductInOrder> getCart(Principal principal) {
        Client client = clientRepository.findByUsername(principal.getName().toLowerCase());
        return client.getCart().getProductsInOrder();
    }


    @PostMapping("/add")
    public boolean addToCart(@RequestBody ItemForm dataFromform, Principal principal) {
        Product productInfo;
        productInfo = productService.findByCode(dataFromform.getProductCode());
        try {

            //mergeCart(Collections.singleton(new ProductInOrder(productInfo, dataFromform.getQuantity())), dataFromform.getClient(), principal);
            ProductInOrder productInOrder = new ProductInOrder(productInfo, dataFromform.getQuantity());
            dataFromform.setLocalCartProductsInOrder(Collections.singleton(productInOrder));
            mergeCart(dataFromform,principal);
//            dataFromform.getProductInOrders().add(productInOrder);
//            mergCart(dataFromform.getProductInOrders(), dataFromform.getClient(), principal);
        } catch (Exception e) {
            log.info(e.getMessage().toString());
            return false;
        }
        return true;
    }

    @PutMapping("/{itemId}")
    public ProductInOrder modifyItem(@PathVariable("itemId") String itemId, @RequestBody Integer quantity, Principal principal) {
        Client client = clientRepository.findByUsername(principal.getName().toLowerCase());
        productInOrderService.update(itemId, quantity, client);
        return productInOrderService.findOne(itemId, client);
    }

    @DeleteMapping("/{itemId}")
    public void deleteItem(@PathVariable("itemId") String itemId, Principal principal) {
        Client client = clientRepository.findByUsername(principal.getName().toLowerCase());
        cartService.delete(itemId, client);
        // flush memory into DB
    }


    @PostMapping("/checkout")
    public ResponseEntity checkout(Principal principal) {
        Client client = clientRepository.findByUsername(principal.getName().toLowerCase());

        cartService.checkout(client);
        return ResponseEntity.ok(null);
    }


    @PostMapping("/orders")
    public Commande order(@RequestBody OrderForm orderForm) {
        Client client = new Client();
        client.setFirstName(orderForm.getClient().getFirstName());
        client.setEmail(orderForm.getClient().getEmail());
        client.setAddress(orderForm.getClient().getAddress());
        client.setUsername(orderForm.getClient().getUsername());
        client = clientRepository.save(client);
        System.out.println(client.getId());

        Commande order = new Commande();
        order.setClient(client);
        order.setDateCommande(new Date());
        order = commandeRepository.save(order);
        double totale = 0;

        for (OrderProduct op : orderForm.getProducts()) {
            CommandeItem orderItem = new CommandeItem();
            orderItem.setCommande(order);
            Product product = productRepository.findById(op.getId()).get();
            orderItem.setProduct(product);
            orderItem.setBuyingPrice(product.getCurrentPrice());
            orderItem.setNbProduct(product.getQuantity());
            commandeItemRepository.save(orderItem);
            totale += op.getQuantity() * product.getCurrentPrice();
        }
        order.setOrderTotalPrices(totale);

        return order;
    }


}
