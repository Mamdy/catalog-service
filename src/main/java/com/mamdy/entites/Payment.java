package com.mamdy.entites;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Date;

@Data
@NoArgsConstructor
@Document
public class Payment {
    private String id;
    private Date datePayment;
    private long cadNumber;
    private String cardType;
    private Commande commande;
}
