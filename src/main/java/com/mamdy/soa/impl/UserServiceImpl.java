package com.mamdy.soa.impl;


import com.mamdy.dao.CartRepository;
import com.mamdy.dao.ClientRepository;
import com.mamdy.dao.UserRepository;
import com.mamdy.entites.User;
import com.mamdy.enums.ResultEnum;
import com.mamdy.exception.MyException;
import com.mamdy.soa.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collection;

@Service
//@DependsOn("passwordEncoder")
public class UserServiceImpl implements UserService {
    BCryptPasswordEncoder bcpe = getBCPasswordEncoder();
    @Autowired
    UserRepository userRepository;
    @Autowired
    CartRepository cartRepository;
    @Autowired
    ClientRepository clientRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public User findOne(String email) {
        return userRepository.findByEmail(email);
    }

    @Override
    public Collection<User> findByRole(String role) {
        return userRepository.findAllByRole(role);
    }

    @Override
    @Transactional
    public User save(User user) {
        //register
        //BCryptPasswordEncoder bce = new BCryptPasswordEncoder();

        user.setPassword(bcpe.encode(user.getPassword()));
        try {
            return userRepository.save(user);

            // Client client = new Client();

            // initial Cart
//            Cart savedCart = cartRepository.save(new Cart(client));
//            savedUser.setCart(savedCart);
//            return userRepository.save(savedUser);

        } catch (Exception e) {
            throw new MyException(ResultEnum.VALID_ERROR);
        }

    }

    @Override
    @Transactional
    public User update(User user) {
        User oldUser = userRepository.findByEmail(user.getEmail());
        oldUser.setPassword(bcpe.encode(user.getPassword()));
        oldUser.setName(user.getName());
        oldUser.setPhone(user.getPhone());
        oldUser.setAddress(user.getAddress());
        return userRepository.save(oldUser);
    }

    @Bean
    BCryptPasswordEncoder getBCPasswordEncoder() {

        return new BCryptPasswordEncoder();
    }

}
