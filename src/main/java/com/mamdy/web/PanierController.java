package com.mamdy.web;

import com.mamdy.dao.PanierRepository;
import com.mamdy.dao.ProductRepository;
import com.mamdy.entites.Panier;
import com.mamdy.entites.Product;
import com.mamdy.utils.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

@RestController
public class PanierController {

    @Autowired
    private PanierRepository panierRepo;

    @Autowired
    private ProductRepository prodRepo;



//    @PostMapping("/addProductToCart")
//    public ResponseEntity<Response> addProductToCart(String productId,String custumerEmail) throws IOException {
//        Product product = prodRepo.findById(productId).get();
//        Panier panier = new  Panier();
//        panier.addArticle(product, 1);
//
//
//        panierRepo.
//
//
//    }

//    @GetMapping("/viewPanier")
//    public ResponseEntity<Response> viewCustomerCart(String custumerEmail) throws IOException {
//        List<Panier> paniers = panierRepo.findAllByEmail(custumerEmail);
//
//        if (paniers != null) {
//            return new ResponseEntity<Response>(new Response("Product is Saved Successfully"), HttpStatus.OK);
//        }
//
//        return null;
//    }
}
