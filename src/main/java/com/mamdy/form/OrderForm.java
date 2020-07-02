package com.mamdy.form;

import com.mamdy.entites.Client;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class OrderForm {

    private Client client;
    private List<OrderProduct> products = new ArrayList<>();

}