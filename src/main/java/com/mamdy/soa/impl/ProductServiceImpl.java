package com.mamdy.soa.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.mamdy.dao.CategoryRepository;
import com.mamdy.dao.ProductRepository;
import com.mamdy.entites.Category;
import com.mamdy.entites.Product;
import com.mamdy.soa.interf.ProductService;

@Service
@Transactional
public class ProductServiceImpl implements ProductService {
	@Autowired
	private ProductRepository productRepository;
	@Autowired
	private CategoryRepository categoryRepository;

	@Override
	public void saveProductService(String name, String marque, String description, double price, int quantity,
			Category category) {
		Product p = new Product();
		p.setName(name);
		p.setBrand(marque);
		p.setDescription(description);
		p.setPrice(price);
		p.setQuantite(quantity);
		p.setCategory(category);
		productRepository.save(p);
		// mise à jour liste des produits du category passé en parametre
		if (category != null) {
			category.getProducts().add(p);
			categoryRepository.save(category);
		}

	}
	

}
