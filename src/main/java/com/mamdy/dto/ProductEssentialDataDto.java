package com.mamdy.dto;

import lombok.Data;

@Data
public class ProductEssentialDataDto {
    private String name;
    private String brand;
    private String description;
    private double price;
    private int stock;
    private String category;
}