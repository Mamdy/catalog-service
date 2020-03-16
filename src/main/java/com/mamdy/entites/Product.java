package com.mamdy.entites;


import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

import org.springframework.data.annotation.Transient;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.web.multipart.MultipartFile;

import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Document
@Data
@AllArgsConstructor
@NoArgsConstructor
//@ToString
public class Product {
	private String id;
	private String code;
	
	@NotBlank(message= "Please enter the product Name!")
	@Size(min = 2, max = 20)
	private String name;
	@Min(value = 1)
	private double price;
	@NotBlank(message= "Please enter th product brand!")
	@Size(min = 2, max = 20)
	private String brand;
	@JsonIgnore
	@NotBlank(message= "Please enter the product description !")
	private String description;
	@Min(value = 1)
	private double currentPrice;
	private boolean promotion;
	private boolean selected;
	private boolean available;
	private boolean active;
	@Min(value = 1)
	private int quantity;
	private String photoUrl;
	@JsonIgnore
	private int supplierId;
	private int purchases;
	private int views;
	private byte[] photo;
	private String fileName;
	
	@Transient
	private MultipartFile file;


	@DBRef
	private Category category;

}
