package com.mamdy;

import com.mamdy.dao.*;
import com.mamdy.entites.*;
import net.bytebuddy.utility.RandomString;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.mongo.MongoAutoConfiguration;
import org.springframework.context.annotation.Bean;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Stream;

@SpringBootApplication
public class CatalogServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(CatalogServiceApplication.class, args);

    }

    @Bean
    CommandLineRunner start(CategoryRepository categoryRepository, ProductRepository productRepository, UserRepository userRepository, CartRepository cartRepository, ProductInOrderRepository productInOrderRepository, ClientRepository clientRepository) {
        return args -> {

            Set<ProductInOrder> productsInOrders = new HashSet<>();
            categoryRepository.deleteAll();
            Stream.of("C1 Ordinateurs", "C2 Imprimantes", "C3 Maison", "C4 Telephone", "C5 Electro-Menager","C6 Alimentation","C7 Mecanique","C8 Voyage","C9 Losirs","C10 Tourisme").forEach(c -> {
                categoryRepository.save(new Category(c.split(" ")[0], c.split(" ")[1], null, null, true, new ArrayList<>()));


            });
            categoryRepository.findAll().forEach(System.out::println);


            productRepository.deleteAll();
            Category c1 = categoryRepository.findById("C1").get();
            int i = 0;
            for (String s : Arrays.asList("DELL Dual-Core", "ASUS-P1", "DELL-P2", "ASUS-P2")) {
                i = i + 1;

                Product p = productRepository.save(new Product(null, RandomString.make(5), s, Math.random() * 1000, RandomString.make(5), "Le lorem ipsum est, en imprimerie, une suite de mots sans signification utilisée à titre provisoire pour calibrer une mise en page, le texte définitif venant remplacer le faux-texte dès qu'il est prêt ou que la mise en page est achevée. Généralement, on utilise un texte en faux latin, le Lorem ipsum ou Lipsum.", Math.random() * 1000, false, false, true, true, (int) (Math.random() * 10), (int) (Math.random() * 10), (int) (Math.random() * 10), "Ordinateur" + i, (int) (Math.random() * 10), (int) (Math.random() * 10), 0, null, null, null, c1));

                c1.getProducts().add(p);
                categoryRepository.save(c1);


            }

            Category c2 = categoryRepository.findById("C2").get();
            int j = 0;
            for (String s : Arrays.asList("Cannon-P1", "EPSON-P2")) {
                j = j + 1;
                Product p = productRepository.save(new Product(null, RandomString.make(5), s, Math.random() * 1000, RandomString.make(5), "Le lorem ipsum est, en imprimerie, une suite de mots sans signification utilisée à titre provisoire pour calibrer une mise en page, le texte définitif venant remplacer le faux-texte dès qu'il est prêt ou que la mise en page est achevée. Généralement, on utilise un texte en faux latin, le Lorem ipsum ou Lipsum.", Math.random() * 1000, false, false, true, false, (int) (Math.random() * 10), (int) (Math.random() * 10), (int) (Math.random() * 10), "Imprimante" + j, (int) (Math.random() * 10), (int) (Math.random() * 10), 1, null, null, null, c2));
                //Mise à jour de la categorie c2
                c2.getProducts().add(p);
                categoryRepository.save(c2);
            }

            Category c3 = categoryRepository.findById("C3").get();
            Stream.of("Duplex-P1", "Duplex-P1", "Duplex-P3").forEach(name -> {
                Product p = productRepository.save(new Product(null, RandomString.make(5), name, Math.random() * 1000, RandomString.make(5), RandomString.make(5), Math.random() * 1000, false, false, true, true, (int) (Math.random() * 10), (int) (Math.random() * 10), (int) (Math.random() * 10), "coach_mobile", (int) (Math.random() * 10), (int) (Math.random() * 10), 1, null, null, null, c3));
                //Mise à jour de la categorie c3
                c3.getProducts().add(p);
                categoryRepository.save(c3);
            });

            Category c4 = categoryRepository.findById("C4").get();
            Stream.of("SAMSUNG-S10", "SAMSUNG-S11", "SAMSUNG-S12").forEach(name -> {
                Product p = productRepository.save(new Product(null, RandomString.make(5), name, Math.random() * 1000, RandomString.make(5), "Le lorem ipsum est, en imprimerie, une suite de mots sans signification utilisée à titre provisoire pour calibrer une mise en page, le texte définitif venant remplacer le faux-texte dès qu'il est prêt ou que la mise en page est achevée. Généralement, on utilise un texte en faux latin, le Lorem ipsum ou Lipsum.", Math.random() * 1000, false, false, true, true, (int) (Math.random() * 10), (int) (Math.random() * 10), (int) (Math.random() * 10), "samsung", (int) (Math.random() * 10), (int) (Math.random() * 10), 2, null, null, null, c4));
                //Mise à jour de la categorie c4
                c4.getProducts().add(p);
                categoryRepository.save(c4);
            });

            Category c5 = categoryRepository.findById("C5").get();
            Stream.of("HAIR-P13", "WHIRLPOOL-14").forEach(name -> {
                Product p = productRepository.save(new Product(null, RandomString.make(5), name, Math.random() * 1000, RandomString.make(5), "Le lorem ipsum est, en imprimerie, une suite de mots sans signification utilisée à titre provisoire pour calibrer une mise en page, le texte définitif venant remplacer le faux-texte dès qu'il est prêt ou que la mise en page est achevée. Généralement, on utilise un texte en faux latin, le Lorem ipsum ou Lipsum.", Math.random() * 1000, false, false, true, true, (int) (Math.random() * 10), (int) (Math.random() * 10), (int) (Math.random() * 10), "machinelaver", (int) (Math.random() * 10), (int) (Math.random() * 10), 3, null, null, null, c5));
                //Mise à jour de la categorie c5
                c5.getProducts().add(p);
                categoryRepository.save(c5);
            });

            clientRepository.deleteAll();
            Client client1 = new Client();
            client1.setUsername("balphamamoudoutest@yahoo.fr");
            client1.setFirstName("test");
            client1.setLastName("test");
            client1.setEmail("balphamamoudoutest@yahoo.fr");
            client1.setAddress("9testtetetstt");
            client1.setPhone("25252525");

            client1 = clientRepository.save(client1);

            productInOrderRepository.deleteAll();
            cartRepository.deleteAll();
            Cart cart1 = new Cart();
            cart1.setProductsInOrder(new HashSet<>());
            cart1.setClient(client1);
            cart1 = cartRepository.save(cart1);

            cart1 = cartRepository.findById(cart1.getId()).get();

            //creation liste productInOrder
            Product product = productRepository.save(new Product(null, RandomString.make(5), "P15", Math.random() * 1000, RandomString.make(5), RandomString.make(5), Math.random() * 1000, false, false, true, false, (int) (Math.random() * 10), (int) (Math.random() * 10), (int) (Math.random() * 10), "Imprimante" + j, (int) (Math.random() * 10), (int) (Math.random() * 10), 1, null, null, null, c2));
            ProductInOrder productInOrder = new ProductInOrder(product, (int) (Math.random() * 10));
            productInOrder.setCart(cart1);
            productInOrder = productInOrderRepository.save(productInOrder);
            System.out.println(productInOrder);
            cart1.getProductsInOrder().add(productInOrder);
            cartRepository.save(cart1);

            client1.setCart(cart1);
            clientRepository.save(client1);

        };

    }
}
