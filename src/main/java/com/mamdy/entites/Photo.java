package com.mamdy.entites;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;
import org.hibernate.validator.constraints.UniqueElements;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import javax.persistence.Column;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@NoArgsConstructor
@Document
@Data
@AllArgsConstructor
public class Photo {
    @Id
    private String id;
    @NotBlank
    @NotNull
    @UniqueElements
    private String name;

    @Column(name = "type")
    private String type;
    @Column(name = "image", length = 1000)
    private byte[] img;
    @JsonIgnore
    @DBRef
    private Product product;
}
