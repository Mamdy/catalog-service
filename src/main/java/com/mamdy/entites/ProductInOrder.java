package com.mamdy.entites;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import javax.persistence.Id;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.util.Objects;


@Document
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProductInOrder {
    @Id
    private String id;

    @JsonIgnore
    @DBRef
    private Cart cart;

    @DBRef
    @JsonIgnore
    private OrderMain orderMain;

    private String productCode;

    @NotEmpty
    private String productId;

    @NotEmpty
    private String productName;

    @NotNull
    private String productDescription;

    private String productIcon;


    private String categoryType;


    private @Min(value = 1) BigDecimal productPrice;


    @Min(0)
    private Integer productStock;

    @Min(1)
    private Integer count;


    public ProductInOrder(Product product, Integer quantity) {
        this.productId = product.getId();
        this.productCode = product.getCode();
        this.productName = product.getName();
        this.productDescription = product.getDescription();
        this.productIcon = product.getPhotoUrl().get(0);
        this.categoryType = product.getCategory().getName();
        this.productPrice = new BigDecimal(product.getPrice());
        this.productStock = product.getProductStock();
        this.count = quantity;
    }

    @Override
    public String toString() {
        return "ProductInOrder{" +
                "id=" + id +
                ", productId='" + productId + '\'' +
                ", productName='" + productName + '\'' +
                ", productDescription='" + productDescription + '\'' +
                ", productIcon='" + productIcon + '\'' +
                ", categoryType=" + categoryType +
                ", productPrice=" + productPrice +
                ", productStock=" + productStock +
                ", count=" + count +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        ProductInOrder that = (ProductInOrder) o;
        return Objects.equals(id, that.id) &&
                Objects.equals(productId, that.productId) &&
                Objects.equals(productName, that.productName) &&
                Objects.equals(productDescription, that.productDescription) &&
                Objects.equals(productIcon, that.productIcon) &&
                Objects.equals(categoryType, that.categoryType) &&
                Objects.equals(productPrice, that.productPrice);
    }

    @Override
    public int hashCode() {

        return Objects.hash(super.hashCode(), id, productId, productName, productDescription, productIcon, categoryType, productPrice);
    }
}
