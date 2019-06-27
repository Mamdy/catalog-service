package com.mamdy.entites;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

@Document
@Data
@AllArgsConstructor
@NoArgsConstructor
public class LignePanier {
    @Id
    private Long id;
    @DBRef
    private Product product;
    private int panierId;
    private int nbProduct;
    private double total;
    private double buyingPrice;
    private boolean available = true;
   }
