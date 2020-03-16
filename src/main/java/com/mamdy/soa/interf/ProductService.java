package com.mamdy.soa.interf;

import java.io.IOException;

import org.springframework.web.multipart.MultipartFile;

import com.mamdy.entites.Category;
import com.mamdy.entites.Product;

public interface ProductService {
    //Product saveProduct(Product product,String categoryId);
	//Product saveProductServerAndDataBase(String name, String marque, String description,  double price, int quantity,String fileName, Category category);
    Product saveProduct(String name, String brand, String description,  double price, int quantity,Category category) throws IOException;
	Product saveProductInServerAndDataBase(String name, String marque, String description, double price, int quantity,
			MultipartFile file, String fileName, Category category) throws IOException;
	Product saveProductInServerAndDataBase(String name, String marque, String description, double price, int quantity,
			String fileName, Category category) throws IOException;
	Product saveProduct(String name, String brand, String description, double price, int quantity, MultipartFile file,
			Category category) throws IOException;
}
