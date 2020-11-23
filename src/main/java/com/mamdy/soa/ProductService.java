package com.mamdy.soa;

import com.mamdy.entites.Category;
import com.mamdy.entites.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

public interface ProductService {
	//Product saveProduct(Product product,String categoryId);
	//Product saveProductServerAndDataBase(String name, String marque, String description,  double price, int quantity,String fileName, Category category);
	Product saveProduct(String name, String brand, String description, double price, int quantity, Category category) throws IOException;

	Product saveProductInServerAndDataBase(String name, String marque, String description, double price, int quantity,
										   MultipartFile file, String fileName, Category category) throws IOException;

	Product saveProductInServerAndDataBase(String name, String marque, String description, double price, int quantity,
										   String fileName, Category category) throws IOException;

	Product saveProduct(String name, String brand, String description, double price, int quantity, MultipartFile file,
						Category category) throws IOException;

	// increase stock
	void increaseStock(String productId, int amount);

	Product findOne(String productId);

	Product findByCode(String code);

	//decrease stock
	void decreaseStock(String productCode, int amount);

	Product offSale(String productId);

	Product onSale(String productId);

	Product update(Product productInfo);

	Product save(Product productInfo);

	void delete(String productId);

	// All products
	Page<Product> findAll(Pageable pageable);
}
