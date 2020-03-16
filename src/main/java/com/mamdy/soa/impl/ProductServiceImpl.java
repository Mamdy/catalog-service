package com.mamdy.soa.impl;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

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
	public Product saveProductInServerAndDataBase(String name, String marque, String description, double price, int quantity,
			 String fileName, Category category) throws IOException {
		Product p = new Product();
		p.setName(name);
		p.setBrand(marque);
		p.setDescription(description);
		p.setPrice(price);
		p.setQuantity(quantity);
		//p.setFile(file);
		//p.setPhoto(file.getBytes());
		p.setFileName(fileName);
		p.setPhotoUrl(fileName);
		p.setCategory(category);
		p.setActive(true);
		p.setSupplierId(1);
		productRepository.save(p);
		// mise à jour liste des produits du category passé en parametre
		if (category != null) {
			category.getProducts().add(p);
			category.setActive(true);
			categoryRepository.save(category);
		}
		return p;

	}

	@Override
	public Product saveProduct(String name, String brand, String description, double price, int quantity,
			Category category) throws IOException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Product saveProductInServerAndDataBase(String name, String marque, String description, double price,
			int quantity, MultipartFile file, String fileName, Category category) throws IOException {
		Product p = new Product();
		p.setName(name);
		p.setBrand(marque);
		p.setDescription(description);
		p.setPrice(price);
		p.setQuantity(quantity);
		p.setFile(file);
		p.setPhoto(file.getBytes());
		p.setFileName(fileName);
		p.setPhotoUrl(fileName);
		p.setCategory(category);
		p.setActive(true);
		p.setSupplierId(1);
		productRepository.save(p);
		// mise à jour liste des produits du category passé en parametre
		if (category != null) {
			category.getProducts().add(p);
			category.setActive(true);
			categoryRepository.save(category);
		}
		return p;	}

	@Override
	public Product saveProduct(String name, String brand, String description, double price, int quantity, MultipartFile file, 
			Category category) throws IOException {
		Product p = new Product();
		p.setName(name);
		p.setBrand(brand);
		p.setDescription(description);
		p.setPrice(price);
		p.setQuantity(quantity);
		p.setFile(file);
		p.setFileName(file.getOriginalFilename());
		p.setPhoto(file.getBytes());
		p.setPhotoUrl(file.getOriginalFilename());
		p.setCategory(category);
		p.setActive(true);
		p.setSupplierId(1);
		productRepository.save(p);
		// mise à jour liste des produits du category passé en parametre
		if (category != null) {
			category.getProducts().add(p);
			category.setActive(true);
			categoryRepository.save(category);
		}
		return p;
	}
	

}
