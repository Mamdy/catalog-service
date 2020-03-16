package com.mamdy.utils;

import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import com.mamdy.entites.Product;

public class ProductFormValidator implements Validator{

	@Override
	public boolean supports(Class<?> clazz) {
		return Product.class.equals(clazz);
	}

	@Override
	public void validate(Object target, Errors errors) {
		Product product  = (Product) target;
		
		//soit le fichier à été selectionné ou non;
		/*if(product.getFile() == null  || product.getFile().getOriginalFilename().equals("")) {
			errors.rejectValue("file", null, "Veuillez choisir que des fichiers images");
			return;
		}
		
		if(! (product.getFile().getContentType().equals("image/jpeg")) ||
			 (product.getFile().getContentType().equals("image/png")) ||
			 (product.getFile().getContentType().equals("image/gif"))){
			
			errors.rejectValue("file", null, "Veuillez choisir que des fichiers image");
			return;
		}*/
		
	}

}
