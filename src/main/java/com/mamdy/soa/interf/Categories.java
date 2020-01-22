package com.mamdy.soa.interf;

import java.util.List;

import com.mamdy.entites.Category;

public interface Categories {
	Category saveCategorie(Category category);

	List<Category> getAllcategories();

	Category getCategoryById(int id);

}
