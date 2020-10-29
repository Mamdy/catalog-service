package com.mamdy.entites;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import javax.persistence.ManyToOne;

@Document
@Data
public class CommandeItem {
    @Id
    private Long id;
    @DBRef
    @ManyToOne
    private Product product;

    @DBRef
    @ManyToOne
    private Commande commande;
    private double buyingPrice;
    private int nbProduct;
    private double toatalPrice;


}
