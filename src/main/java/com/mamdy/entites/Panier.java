package com.mamdy.entites;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Document
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Panier {
    @Id
    private String id;
    private double grandTotal;
    private int cartLines;

    private String email;

    private Date dateAdded;

    private int quantity;
    private double price;
    private int productId;

    private String productname;


    //lien du panier avec avec le user
    @DBRef
    private Client client;

    private Map<Long, LignePanier> items = new HashMap<Long, LignePanier>();

    //Methode utilitaire qui permet d'ajouter un produit dans un panier
    public void addArticle(Product p, int quantite) {
        if (items.get(p.getId()) == null) {
            LignePanier lc = new LignePanier();
            /*lc.setProduct(p);
            lc.setQuantite(p.getQuantite());
            lc.setPrice(p.getCurrentPrice());*/
        }else{
            LignePanier lc = items.get(p.getId());
            //lc.setQuantite(p.getQuantite()+quantite);

        }
    }



}
