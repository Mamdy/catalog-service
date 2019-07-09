package com.mamdy.web;

import com.mamdy.dao.CategoryRepository;
import com.mamdy.entites.Category;
import com.mamdy.entites.Product;
import com.mamdy.soa.interf.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Controller
@RestController
@RequestMapping("")
public class CatalogueController {
    @Autowired
    private ProductService productService;
    @Autowired
    private CategoryRepository categoryRepository;

    @PostMapping("/createProduct")
    public Product CreateProduct(@RequestBody Product product, String categoriId){

        //Category c = categoryRepository.findById(categoriId).get();


        return productService.saveProduct(product,categoriId);
    }
}
