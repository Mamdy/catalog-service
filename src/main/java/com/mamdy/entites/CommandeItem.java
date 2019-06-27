package com.mamdy.entites;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;

public class CommandeItem {
    @Id
    private int id;
    @DBRef
    private Product product;

    @DBRef
    private Commande commande;
    private double buyingPrice;
    private int nbProduct;
    private double toatalPrice;


}
