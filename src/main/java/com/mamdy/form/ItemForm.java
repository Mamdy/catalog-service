package com.mamdy.form;

import com.mamdy.entites.Client;
import com.mamdy.entites.ProductInOrder;
import lombok.Data;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotEmpty;
import java.util.Collection;

@Data
public class ItemForm {
    @Min(value = 1)
    private Integer quantity;
    @NotEmpty
    private String productCode;

    private String connectedUsername;

    private Client client;

    private Collection<ProductInOrder> localCartProductsInOrder;
}