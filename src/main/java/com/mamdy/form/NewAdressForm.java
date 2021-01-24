package com.mamdy.form;

import com.mamdy.entites.Client;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class NewAdressForm {
    private String nom;
    private String prenom;
    private String numero;
    private String voie;
    private String codepostal;
    private String ville;
    private String pays;
    private String telephone;

}
