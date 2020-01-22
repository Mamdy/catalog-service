package com.mamdy.soa.interf;

import com.mamdy.entites.Category;
import com.mamdy.entites.Product;

public interface ProductService {
    //Product saveProduct(Product product,String categoryId);
    void saveProductService(String name, String marque, String description,  double price, int quantity,Category category);
}
