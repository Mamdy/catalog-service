package com.mamdy.dao;

import com.mamdy.entites.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import java.util.List;
import java.util.Optional;

//import org.springframework.web.bind.annotation.CrossOrigin;
//CrossOrigin("*")
//@RepositoryRestResource(collectionResourceRel = "products", path = "products")
@RepositoryRestResource
public interface ProductRepository extends MongoRepository<Product, String> {
    @Query("{ 'name' : ?0 }")
    Product findByName(String name);

    Page<Product> findByNameContaining(String name, Pageable pageable);

    List<Product> findByNameLike(String name);
    //Product saveProduct(@Param("product") Product product, String categoryId);
    //Product saveProduct(Product product,String categoryName);

    Optional<Product> findById(String id);

    //
//    Page<Product> findAllByProductStatusOrderByIdAsc(Integer productStatus, Pageable pageable);
//
//    // product in one category
//    Page<Product> findAllByCategoryOrderByIdAsc(Category category, Pageable pageable);
//
    Page<Product> findAllByOrderById(Pageable pageable);

    Product findByCode(String code);

}
