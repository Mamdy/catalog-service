package com.mamdy.security;

import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {

//    @Override
//    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
//        //methode 1: gestions des users par autehenticfiction par session.
//        BCryptPasswordEncoder bcpe = getBCPE();
//        auth.inMemoryAuthentication().withUser("admin").password(bcpe.encode("1234")).roles("ADMIN", "CUSTOMER");
//        auth.inMemoryAuthentication().withUser("user1").password(bcpe.encode("1234")).roles("CUSTOMER");
//
//        //methode 2: enlever la 1ere methode et dire à spring qu'on ne veut plus s'authentifier par session(STATELESS)
//
//
//    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        //  super.configure(http);
        http.csrf().disable();
        //activation du formulaire de login
        // http.formLogin();
        //dire à spring qu'on ne veut plus s'authentifier par session(STATELESS)(la on desactive les session) puis on utlise les token
        http.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);

        //j'utilise le token jwt
        http.authorizeRequests().antMatchers(HttpMethod.GET, "/categories/**").permitAll();
        http.authorizeRequests().antMatchers(HttpMethod.GET, "/products/**").permitAll();
        http.authorizeRequests().antMatchers(HttpMethod.GET, "/order/**").permitAll();
        http.authorizeRequests().antMatchers("/categories/**").hasAnyAuthority("CUSTOMER", "ADMIN");
        http.authorizeRequests().antMatchers("/products/**").hasAnyAuthority("CUSTOMER", "ADMIN");
        http.authorizeRequests().antMatchers("/cart/add").permitAll();
        http.authorizeRequests().antMatchers("/cart/**").permitAll();
        http.authorizeRequests().antMatchers("/order/**").permitAll();
        http.authorizeRequests().antMatchers("/payment/paymentintent/**").permitAll();
        http.authorizeRequests().antMatchers("/payment/confirm/**").permitAll();
        //http.authorizeRequests().antMatchers("/order/**").authenticated();
        http.authorizeRequests().antMatchers("/order/finish/**").permitAll();


        //        http.authorizeRequests().antMatchers(HttpMethod.GET,"/cart/**").permitAll();
//        http.authorizeRequests().antMatchers(HttpMethod.GET,"/cart/add/**").permitAll();http.authorizeRequests().antMatchers("/categories/**").hasAuthority("ADMIN");

        http.authorizeRequests().anyRequest().authenticated();
//        http.authorizeRequests().antMatchers("/cart/add/**").hasAnyAuthority("ROLE_CUSTOMER","ADMIN");
//        //pour toutes autres requête il faut etre authentifier pour pouvoir les executer.

        //http.addFilter(new JWTAuthenticationFilter(authenticationManager()));
        //Mise en place d'un filtre qui va filter la requête(voior si elle contient le token si oui il la signe numeriquement)
        http.addFilterBefore(new JWTAuthorizationFilter(), UsernamePasswordAuthenticationFilter.class);

        // http.authorizeRequests().anyRequest().permitAll();
    }

//    @Bean
//    BCryptPasswordEncoder getBCPE() {
//
//        return new BCryptPasswordEncoder();
//    }
}
