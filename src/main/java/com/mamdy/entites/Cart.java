package com.mamdy.entites;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.HashSet;
import java.util.Set;

@NoArgsConstructor
@Data
@Document
@AllArgsConstructor
public class Cart {
    @Id
    private String id;

    //    @OneToOne(fetch = FetchType.LAZY)
//    @MapsId
//    @JsonIgnore
//    @JoinColumn(name = "email", referencedColumnName = "email")
    @JsonIgnore
    @DBRef
    private Client client = new Client();

    //    @OneToMany(cascade = CascadeType.ALL,
//            fetch = FetchType.LAZY, orphanRemoval = true,
//            mappedBy = "cart")
    @DBRef
    private Set<ProductInOrder> productsInOrder = new HashSet<>();


    public Cart(Client client) {
        this.client = client;
    }


}
