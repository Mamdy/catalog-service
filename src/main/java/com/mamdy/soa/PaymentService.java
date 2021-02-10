package com.mamdy.soa;

import com.mailjet.client.errors.MailjetSocketTimeoutException;
import com.mamdy.dao.CartRepository;
import com.mamdy.dao.ClientRepository;
import com.mamdy.dao.ProductInOrderRepository;
import com.mamdy.dto.PaymentIntentDto;
import com.mamdy.entites.Cart;
import com.mamdy.entites.Client;
import com.mamdy.entites.OrderMain;
import com.mamdy.entites.ProductInOrder;
import com.mamdy.utils.MailJetUtils;
import com.stripe.Stripe;
import com.stripe.exception.StripeException;
import com.stripe.model.PaymentIntent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class PaymentService {
    @Value("${stripe.key.secret}")
    String secretKey;

    @Value("${mailjet.key.public}")
    String mailjetPublicKey;

    @Value("${mailjet.key.secret}")
    String mailjetSecretKey;

    @Autowired
    public OrderService orderService;

    @Autowired
    ClientRepository clientRepository;

    @Autowired
    CartRepository cartRepository;

    @Autowired
    ProductInOrderRepository productInOrderRepository;

    @Autowired
    CartService cartService;

    @Autowired
    ProductService productService;



    public PaymentIntent paymentIntent(final PaymentIntentDto paymentIntentDto) throws StripeException {
        Stripe.apiKey = secretKey;
        List<String> paymentMethodTypes = new ArrayList();
        paymentMethodTypes.add("card");
        Map<String, Object> params = new HashMap<>();
        params.put("amount", paymentIntentDto.getAmount());
        params.put("currency", paymentIntentDto.getCurrency());
        params.put("description", paymentIntentDto.getDescription());
        params.put("payment_method_types", paymentMethodTypes);
        return PaymentIntent.create(params);
    }

    public PaymentIntent confirm(String id, String orderId, String customerMail) throws StripeException, MailjetSocketTimeoutException {
        Client customer = clientRepository.findByUsername(customerMail);
        Stripe.apiKey = secretKey;
        PaymentIntent paymentIntent = PaymentIntent.retrieve(id);
        Map<String, Object> params = new HashMap<>();
        params.put("payment_method", "pm_card_visa");
        paymentIntent = paymentIntent.confirm(params);
        if (paymentIntent != null) {
            //mise à jour du champ status de la commande dans la table des commandes(Order)
            OrderMain order = orderService.finish(orderId);
            if (order != null) {
                //envois de mail pour notifier le client de sa commande
                MailJetUtils.sendEmail(
                        "balphamamoudou2013@gmail.com",
                        customerMail,
                        order.getNumOrder(),
                        "Confirmation Commande ",
                        "Bonjour " + customer.getFirstName(),
                        mailjetPublicKey,
                        mailjetSecretKey
                );
                //vider le panier du client en base pour ce/ces productsInOrderes commandés et mettre à jour le stock des produits restant

                Set<ProductInOrder> setPio = order.getProducts();
                setPio.forEach(productInOrder -> {
                    this.productService.decreaseStock(productInOrder.getProductCode(), productInOrder.getCount());
                    //supprimer les produits commandé du panier
                    this.cartService.delete(productInOrder.getProductCode(), customer);
                });
            }

        }
        return  paymentIntent;
    }

    public PaymentIntent cancel(String id) throws StripeException {
        Stripe.apiKey = secretKey;
        PaymentIntent paymentIntent = PaymentIntent.retrieve(id);
        paymentIntent.cancel();
        return paymentIntent;
    }
}
