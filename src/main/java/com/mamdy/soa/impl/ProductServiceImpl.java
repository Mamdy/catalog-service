package com.mamdy.soa.impl;

import com.mamdy.dao.CategoryRepository;
import com.mamdy.dao.ProductRepository;
import com.mamdy.entites.Category;
import com.mamdy.entites.Product;
import com.mamdy.soa.interf.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@Transactional
public class ProductServiceImpl implements ProductService {
    @Autowired
    private ProductRepository productRepository;
    @Autowired
    private CategoryRepository categoryRepository;

    @Override
    public Product saveProduct(Product product,String categoryId) {
        Category c = categoryRepository.findById(categoryId).get();
        if (c!=null){
            product.setCategory(c);
            //productRepository.save(product);
            c.getProducts().add(product);
            //mise à jours

            categoryRepository.save(c);
        }
        return productRepository.save(product);
    }
}
