package com.mamdy.entites;

import org.springframework.data.mongodb.core.mapping.DBRef;

import java.util.ArrayList;
import java.util.Collection;

public class Client {
    private Long idClient;
    private String nameClient;
    private String adresse;
    private String email;
    private String tel;
    @DBRef
    private Collection<Commande> commandes = new ArrayList<>();

}
