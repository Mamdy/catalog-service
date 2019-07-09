package com.mamdy.entites;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.web.multipart.MultipartFile;

@Document
@Data
@AllArgsConstructor
@NoArgsConstructor
//@ToString
public class Product {
    private String id;
    private String code;
    private String name;
    private double price;
    private String brand;
    private String description;
    private double currentPrice;
    private boolean promotion;
    private boolean selected;
    private boolean available;
    private boolean active;
    private int quantite;
    private String photoUrl;
    private int supplierId;
    private int purchases;
    private MultipartFile file;
    private int views;

    @DBRef
    private Category category;


}
