package com.mamdy.entites;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.validator.constraints.UniqueElements;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.Collection;

@Document
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Client {
    @Id
    private String id;
    private String firstName;
    private String lastName;
    private String address;
    private String codePostal;
    private String ville;
    private String pays;
    @NotBlank
    @NotNull
    @UniqueElements
    private String email;
    private String phone;

    @JsonIgnore  // fix bi-direction toString() recursion problem
    @DBRef
    private Cart cart;

}


