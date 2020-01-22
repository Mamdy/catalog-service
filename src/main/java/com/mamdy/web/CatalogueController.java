package com.mamdy.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.mamdy.dao.CategoryRepository;
import com.mamdy.entites.Category;
import com.mamdy.soa.interf.ProductService;

import lombok.Data;

@Controller
@RestController
@RequestMapping("")
public class CatalogueController {
	@Autowired
	private ProductService productService;
	@Autowired
	private CategoryRepository categoryRepository;

	@PostMapping("/createProduct")
	public void CreateProduct(@RequestBody ProductFormData productFormData) {

		String categoryName = productFormData.getCategory();
		Category c = categoryRepository.findByName(categoryName);

		if (c != null) {

			productService.saveProductService(productFormData.getName(), productFormData.getMarque(),
					productFormData.getDescription(), productFormData.getPrice(), productFormData.getQuantity(), c);
		}
	}

}

@Data
class ProductFormData {
	private String name;
	private String marque;
	private String description;
	private double price;
	private int quantity;
	private String category;
}
