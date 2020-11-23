package com.mamdy.soa;

import com.mailjet.client.errors.MailjetSocketTimeoutException;
import com.mamdy.dto.PaymentIntentDto;
import com.mamdy.utils.MailJetUtils;
import com.stripe.Stripe;
import com.stripe.exception.StripeException;
import com.stripe.model.PaymentIntent;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class PaymentService {
    @Value("${stripe.key.secret}")
    String secretKey;

    @Value("${mailjet.key.public}")
    String mailjetPublicKey;

    @Value("${mailjet.key.secret}")
    String mailjetSecretKey;

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

    public PaymentIntent confirm(String id) throws StripeException, MailjetSocketTimeoutException {
        Stripe.apiKey = secretKey;
        PaymentIntent paymentIntent = PaymentIntent.retrieve(id);
        Map<String, Object> params = new HashMap<>();
        params.put("payment_method", "pm_card_visa");
        paymentIntent = paymentIntent.confirm(params);
        if(paymentIntent != null) {
            //envois de mail pour notifier le client de sa commande
            MailJetUtils.sendEmail(
                    "balphamamoudou2013@gmail.com",
                    "balphamamoudou2013@gmail.com",
                    "Votre Commande ",
                    "Bonjour, Votre Commande num xxxx vient d'etre effectuer sur notre Site et vous voux sera expedier dans les delais." +
                            "Merci de votre Confiance et à Bientot",
                    mailjetPublicKey,
                    mailjetSecretKey

            );
        }
        return paymentIntent;
    }

    public PaymentIntent cancel(String id) throws StripeException {
        Stripe.apiKey = secretKey;
        PaymentIntent paymentIntent = PaymentIntent.retrieve(id);
        paymentIntent.cancel();
        return paymentIntent;
    }
}
