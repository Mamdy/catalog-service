package com.mamdy.entites;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.HashSet;
import java.util.Set;

@NoArgsConstructor
@Getter
@Setter
@Document
@AllArgsConstructor
public class Cart {
    @Id
    private String id;

    @JsonIgnore
    @DBRef
    private Client client = new Client();

    @DBRef
    private Set<ProductInOrder> productsInOrder = new HashSet<>();


    public Cart(Client client) {
        this.client = client;
    }


}
