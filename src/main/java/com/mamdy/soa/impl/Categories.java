package com.mamdy.soa.impl;

import com.mamdy.entites.Category;
import com.mamdy.entites.Product;

import java.util.List;

public interface Categories {
    Category saveCategorie(Category category);
    List<Category> getAllcategories();
    Category getCategoryById(int id);

}
