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
import org.springframework.security.core.Authentication;
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

    @PostMapping("")
    public ResponseEntity<Cart> mergeCart(@RequestBody Collection<ProductInOrder> productInOrders, Principal principal) {
        //si authentification est reussi on genere le token
        // User user1 = new User(userDetails.getUsername(),userDetails.getPassword());
        Client client = clientRepository.findByUsername(principal.getName().toLowerCase());
        if (client == null) {
            client = new Client();
            client.setUsername(principal.getName());
            client.setName("Mamdy");
            client.setEmail("balphamamoudou2013@gmail.com");
            client.setAdresse("04 allée Pauline Isabelle Utiles, Nantes");
            client = clientRepository.save(client);


            Cart cart = new Cart();
            cart.setProductsInOrder(new HashSet<>());
            cart.setClient(client);
            cart = cartRepository.save(cart);

            client.setCart(cart);
            client = clientRepository.save(client);


//            ProductInOrder po = productInOrders.stream().findFirst().get();
//            po.setCart(cart);
//            po = productInOrderRepository.save(po);
//            cart.getProductsInOrder().add(po);
//            cart = cartRepository.save(cart);
//


        }

        try {
            cartService.mergeLocalCart(productInOrders, client);
        } catch (Exception e) {
            ResponseEntity.badRequest().body("Merge Cart Failed");
        }
        return ResponseEntity.ok(cartService.getCart(client));
    }

    @GetMapping("")
    public Set<ProductInOrder> getCart(Principal principal) {
        Client client = clientRepository.findByUsername(principal.getName().toLowerCase());
        Set<ProductInOrder> products = client.getCart().getProductsInOrder();
        products.forEach(po -> {
            System.out.println(po);
        });

        return products;
    }


//    @GetMapping("/viewCart")
//    public ResponseEntity<CartResp>  getLoggedUserCart(@RequestHeader(name = WebConstants.USER_NAME)String userName) {
//        Client client = clientRepository.findByUserName(userName);
//        CartResp cartResp = new CartResp();
//        return cartService.getCart(client);
//    }

    @PostMapping("/add")
    public boolean addToCart(@RequestBody ItemForm form, Principal principal) {
        Product productInfo;
        productInfo = productService.findByCode(form.getProductCode());
        try {
            mergeCart(Collections.singleton(new ProductInOrder(productInfo, form.getQuantity())), principal);
        } catch (Exception e) {
            log.info(e.getMessage().toString());
            return false;
        }
        return true;
    }

    @PutMapping("/{itemId}")
    public ProductInOrder modifyItem(@PathVariable("itemId") String itemId, @RequestBody Integer quantity, Principal principal) {
        User user = userService.findOne(principal.getName());
        productInOrderService.update(itemId, quantity, user);
        return productInOrderService.findOne(itemId, user);
    }

    @DeleteMapping("/{itemId}")
    public void deleteItem(@PathVariable("itemId") String itemId, Authentication authResult) {
        org.springframework.security.core.userdetails.User userDetails = (org.springframework.security.core.userdetails.User) authResult.getPrincipal();
        User user = userService.findOne(userDetails.getUsername());
        cartService.delete(itemId, user);
        // flush memory into DB
    }


    @PostMapping("/checkout")
    public ResponseEntity checkout(Authentication authResult) {
        org.springframework.security.core.userdetails.User userDetails = (org.springframework.security.core.userdetails.User) authResult.getPrincipal();
        User user = userService.findOne(userDetails.getUsername());// Email as username
        cartService.checkout(user);
        return ResponseEntity.ok(null);
    }


    @PostMapping("/orders")
    public Commande order(@RequestBody OrderForm orderForm) {
        Client client = new Client();
        client.setName(orderForm.getClient().getName());
        client.setEmail(orderForm.getClient().getEmail());
        client.setAdresse(orderForm.getClient().getAdresse());
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
