package com.mamdy.entites;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;

@Document
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Commande {
    @Id
    private Long idCommande;
    private Date dateCommande;
    private double orderTotalPrices;
    private int nbOrderCount;
    @DBRef
    private Collection<CommandeItem> orderItems = new ArrayList<>();

    @DBRef
    private Client client;
}
