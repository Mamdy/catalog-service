package com.mamdy.form;

import com.mamdy.entites.Client;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class NewAdressForm {
    private String Nom;
    private String prenom;
    private String adress;
    private String codePostale;
    private String phone;
}
