package com.mamdy.soa.impl;

import com.mamdy.entites.Category;
import com.mamdy.entites.Product;
import com.mamdy.soa.CategoryService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CategoryServiceImpl implements CategoryService {
    @Override
    public Category saveCategorie(Category category) {
        return null;
    }

    @Override
    public List<Category> getAllcategories() {
        return null;
    }

    @Override
    public Category getCategoryById(int id) {
        return null;
    }

    @Override
    public Category findByCategoryType(Product product) {
        return product.getCategory();
    }
}
