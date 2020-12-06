package com.mamdy.utils;

import com.mailjet.client.ClientOptions;
import com.mailjet.client.MailjetClient;
import com.mailjet.client.MailjetRequest;
import com.mailjet.client.MailjetResponse;
import com.mailjet.client.errors.MailjetException;
import com.mailjet.client.errors.MailjetSocketTimeoutException;
import com.mailjet.client.resource.Emailv31;
import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.util.StringUtils;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

@Slf4j
@UtilityClass
public class MailJetUtils {

    /**
     * @param sender           l'emeteur du message
     * @param recipient        le destinataire du mail
     * @param subject          l'objet du mail
     * @param body             le contenu du mail
     * @throws MailjetSocketTimeoutException Exception lié au timeout de la socket
     */
    public void sendEmail(final String sender,
                          final String recipient,
                          final String subject,
                          String body,
                          final String mailJetApiKey,
                          final String mailJetSecretKey
                         ) throws MailjetSocketTimeoutException {


        final MailjetClient client = new MailjetClient(
                mailJetApiKey, mailJetSecretKey, new ClientOptions("v3.1"));

        final MailjetResponse response;

        final LocalDateTime ldt = LocalDateTime.now();

        final String date = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm", Locale.ENGLISH).format(ldt);

        final MailjetRequest request;
        request = new MailjetRequest(Emailv31.resource)
                .property(Emailv31.MESSAGES, new JSONArray()
                        .put(new JSONObject()
                                .put(Emailv31.Message.FROM, new JSONObject()
                                        .put("Email", sender)
                                        .put("Name", "E-SHOP224"))
                                .put(Emailv31.Message.TO, new JSONArray()
                                        .put(new JSONObject()
                                                .put("Email", recipient)))
                                .put(Emailv31.Message.SUBJECT, subject + date.substring(0, 10))
                                .put(Emailv31.Message.TEXTPART, body)
                                //.put(Emailv31.Message.HTMLPART, "<h3>  `${body}` Cher Client, merci de visiter notre site <a href=\"https://my-angular-appv1.herokuapp.com/\">E-shop224</a>!</h3><br />A bientot pour d'autres achats! :-)")
                        ));

        try {
            response = client.post(request);
            if (response.getStatus() == 200) {
                log.info("Mail envoyé avec success");
            }
        } catch (final MailjetException mje) {
            log.error("Echec d'envois de mail, Mailjet Exception: " + mje);
        }

    }
}