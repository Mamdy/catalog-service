package com.mamdy.form;

import com.mamdy.entites.Client;
import lombok.Data;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotEmpty;

@Data
public class ItemForm {
    @Min(value = 1)
    private Integer quantity;
    @NotEmpty
    private String productCode;

    private String connectedUsername;

    private Client client;
}