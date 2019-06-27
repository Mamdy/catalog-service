package com.mamdy;

import com.mamdy.dao.CategoryRepository;
import com.mamdy.dao.ProductRepository;
import com.mamdy.entites.Category;
import com.mamdy.entites.Product;
import net.bytebuddy.utility.RandomString;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.stream.Stream;

@SpringBootApplication
public class CatalogServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(CatalogServiceApplication.class, args);

    }

    @Bean
    CommandLineRunner start(CategoryRepository categoryRepository, ProductRepository productRepository) {
        return args -> {
            categoryRepository.deleteAll();
            Stream.of("C1 Ordinateurs", "C2 Imprimantes","C3 Maison", "C4 Telephone","C5 Electro-Menager").forEach(c -> {
                categoryRepository.save(new Category(c.split(" ")[0], c.split(" ")[1],null,null,true, new ArrayList<>()));


            });
            categoryRepository.findAll().forEach(System.out::println);


            productRepository.deleteAll();
            Category c1 = categoryRepository.findById("C1").get();
           int i = 0;
            for (String s : Arrays.asList("P1", "P2", "P3", "P4")) {
                i = i+1;

                Product p = productRepository.save(new Product(null, s, Math.random() * 1000, RandomString.make(5), Math.random() * 1000, false, false, true, (int) (Math.random() * 10), "Ordinateur"+i, (int) (Math.random() * 10), (int) (Math.random() * 10), null, c1));
                c1.getProducts().add(p);
                categoryRepository.save(c1);


            }

            Category c2 = categoryRepository.findById("C2").get();
            int j = 0;
            for (String s : Arrays.asList("P5", "P6")) {
                j = j+1;
                Product p = productRepository.save(new Product(null, s, Math.random() * 1000, RandomString.make(5), Math.random() * 1000, false, false, true, (int) (Math.random() * 10), "Imprimante"+j, (int) (Math.random() * 10), (int) (Math.random() * 10), null, c2));
                //Mise à jour de la categorie c2
                c2.getProducts().add(p);
                categoryRepository.save(c2);
            }

            Category c3 = categoryRepository.findById("C3").get();
            Stream.of("P7", "P8","P9").forEach(name -> {
                Product p = productRepository.save(new Product(null, name, Math.random() * 1000, RandomString.make(5),Math.random()*1000,false, false, true,  (int)(Math.random()*10),"coach_mobile",(int)(Math.random()*10),(int)(Math.random()*10),null, c3));
                //Mise à jour de la categorie c3
                c3.getProducts().add(p);
                categoryRepository.save(c3);
            });

            Category c4 = categoryRepository.findById("C4").get();
            Stream.of("P10", "P11","P12").forEach(name -> {
                Product p = productRepository.save(new Product(null, name, Math.random() * 1000, RandomString.make(5),Math.random()*1000,false, false, true,  (int)(Math.random()*10),"samsung",(int)(Math.random()*10),(int)(Math.random()*10),null, c4));
                //Mise à jour de la categorie c4
                c4.getProducts().add(p);
                categoryRepository.save(c4);
            });

            Category c5 = categoryRepository.findById("C5").get();
            Stream.of("P13", "P14").forEach(name -> {
                Product p = productRepository.save(new Product(null, name, Math.random() * 1000, RandomString.make(5),Math.random()*1000,false, false, true,  (int)(Math.random()*10),"machinelaver",(int)(Math.random()*10),(int)(Math.random()*10),null, c5));
                //Mise à jour de la categorie c5
                c5.getProducts().add(p);
                categoryRepository.save(c5);
            });




            productRepository.findAll().forEach(p->{
                System.out.println(p.toString());
            });
        };
    }
}
