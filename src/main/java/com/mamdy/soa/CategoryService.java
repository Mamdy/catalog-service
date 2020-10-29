package com.mamdy.soa;

import com.mamdy.entites.Category;
import com.mamdy.entites.Product;

import java.util.List;

public interface CategoryService {
	Category saveCategorie(Category category);

	List<Category> getAllcategories();

	Category getCategoryById(int id);

	Category findByCategoryType(Product product);


}
