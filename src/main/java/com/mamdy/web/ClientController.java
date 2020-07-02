package com.mamdy.web;

import com.mamdy.dao.ClientRepository;
import com.mamdy.entites.Client;
import com.mamdy.form.ClientForm;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ClientController {
    @Autowired
    ClientRepository clientRepository;

    @PostMapping("/client")
    Client saveClient(@RequestBody ClientForm clientForm) {
        Client client = clientRepository.findByUsername(clientForm.getUsername());
        if (client == null) {
            client = new Client();
            client.setUsername(clientForm.getUsername());
            client.setEmail(clientForm.getEmail());
            client.setName(clientForm.getName());
            clientRepository.save(client);
        }

        return client;

    }
}
