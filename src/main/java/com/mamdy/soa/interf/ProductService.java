package com.mamdy.soa.interf;

import com.mamdy.entites.Product;

public interface ProductService {
    Product saveProduct(Product product,String categoryId);
}
