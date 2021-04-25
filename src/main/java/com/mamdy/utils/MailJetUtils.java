package com.mamdy.utils;

import com.mailjet.client.ClientOptions;
import com.mailjet.client.MailjetClient;
import com.mailjet.client.MailjetRequest;
import com.mailjet.client.MailjetResponse;
import com.mailjet.client.errors.MailjetException;
import com.mailjet.client.errors.MailjetSocketTimeoutException;
import com.mailjet.client.resource.Emailv31;
import com.mamdy.entites.OrderMain;
import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.util.StringUtils;

import java.text.DecimalFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

@Slf4j
@UtilityClass
public class MailJetUtils {
    private static final String ABS_PATH = "D:/_DVT/workspace/catalog-service/src/main/webapp/WEB-INF/images/brand.jpg";
    private static DecimalFormat df2 = new DecimalFormat("#.##");


    /**
     * @param sender           l'emeteur du message
     * @param recipient        le destinataire du mail
     * @param order            la commande
     * @param subject          l'objet du mail
     * @param body             le contenu du mail
     * @throws MailjetSocketTimeoutException Exception lié au timeout de la socket
     */
    public void sendEmail(final String sender,
                          final String recipient,
                          final OrderMain order,
                          final String subject,
                          final String body,
                          final String mailJetApiKey,
                          final String mailJetSecretKey
                         ) throws MailjetSocketTimeoutException {
        final String logo = "http://localhost:8087/images/brand.jpg";
        //"<img src=\"images/brand.jpg\" alt=\"224-E-SHOP\" width=\"80\" height=\"80\" class=\"d-inline-block align-top brand\">" + "<br>" + "<br>"+ "<br>" + "<br>"
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
                                        .put("Name", "SODENKAMIN"))
                                .put(Emailv31.Message.TO, new JSONArray()
                                        .put(new JSONObject()
                                                .put("Email", recipient)))
                                .put(Emailv31.Message.SUBJECT, subject + date.substring(0, 10))
                                .put(Emailv31.Message.TEXTPART, body)
                                //https://my-224market-appv1.herokuapp.com/followorder/
                                //.put(Emailv31.Message.HTMLPART, "<h3>\""  + body + "\nCher Client, merci de visiter notre site <a href=\"https://my-angular-appv1.herokuapp.com/\">E-shop224</a>!</h3><br />\nA bientot pour d'autres achats! :-)")
                                .put(Emailv31.Message.HTMLPART,
                                        "<img src=\"https://xt96z.mjt.lu/tplimg/xt96z/b/05890/6xmo3.jpeg\" alt=\"logo\" width=\"30\" height=\"30\" class=\"d-inline-block align-top\">"+
                                               "<span style=\"font-size: 25px; color:red\">SODENKAMIN</span>"
                                                + "<br>" + "<br>"+ "<br>" +
                                        "<p style=\"font-size:16px; font-familly:'proxima-nova';\">Bonjour "  + order.getBuyerName() +  ",<br>\n" + "<br>\n" +
                                        "Votre Commande numéro: " + order.getNumOrder() + " d'un montant de : " + df2.format(order.getOrderAmount()) +  "&euro; a été validée." + "<br>" + "<br>" +
                                         "Elle sera traitée dans les plus bref delais." +"<br>" + "<br>" +
                                         "Vous recevrez un e-mail et un sms lorsqu'elle vous sera expédiée." +"<br>" + "<br>" + "<br>" +
                                        "<a href=\"http://localhost:4200/followorder/\" style =\" display:inline-block;text-align:center; line-height:44px; text-decoration:none; color:white; width:300px; height: 15px; font-weight:bold; font-size:16px; background-color: rgba(24, 164, 189, 0.979); font-familly:'proxima-nova'; padding: 10px 80px 10px; border-radius: 25px; margin-left: 315px;\">Suivre ma commande</a></p>" +
                                        "<br>"+ "<br>"+
                                        "<br>"+"Nous vous remercions de votre confiance." +"<br>" + "<br>"+ "<br>" + "<br>"+
                                        "L'équipe SODENKAMIN SUPPORT" + "<br>" +
                                        "SAS au capital de 20.000 euros" + "<br>"+
                                        "Siège Social: 04 Allée Pauline Isabelle Utiles,44200 Nantes" +  "<br>"+
                                        "Immatriculée sous le numéro: 123456789 -Société de financement agréee et contrôlée par l'ACPR"
                                )));

        try {
            response = client.post(request);
            if (response.getStatus() == 200) {
                log.info("Mail envoyé avec success");
            }
        } catch (final MailjetException mje) {
            log.error("Echec d'envois de mail, Mailjet Exception: " + mje);
        }

    }



    /** methode qui permet d'envoyer un lien de changement du mot de passe
     * @param sender           l'emeteur du message
     * @param recipient        le destinataire du mail
     * @param subject          l'objet du mail
     * @param body             le contenu du mail
     * @throws MailjetSocketTimeoutException Exception lié au timeout de la socket
     */
    public void sendResetPasswordLink(final String sender,
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
                                        .put("Name", "SODENKAMIN"))
                                .put(Emailv31.Message.TO, new JSONArray()
                                        .put(new JSONObject()
                                                .put("Email", recipient)))
                                .put(Emailv31.Message.SUBJECT, subject + date.substring(0, 10))
                                .put(Emailv31.Message.TEXTPART, body)
                                .put(Emailv31.Message.HTMLPART,
                                        "<img src=\"https://xt96z.mjt.lu/tplimg/xt96z/b/05890/6xmo3.jpeg\" alt=\"logo\" width=\"30\" height=\"30\" class=\"d-inline-block align-top\">"+
                                                "<span style=\"font-size: 25px; color:red\">SODENKAMIN </span>"
                                                + "<br>" + "<br>"+ "<br>" +
                                        "<p style=\"font-size:16px; font-familly:'proxima-nova';\">"  + body +  "<br>\n" + "<br>\n" +
                                        " Vous avez sollicité un nouveau mot de passe, nous vous invitons à le créer en cliquant sur le bouton ci-dessous:" +"<br>" + "<br>" + "<br>" + "<br>" +
                                        "<a href=\"https://sodenkamin.herokuapp.com/changePassword/\" style =\" display:inline-block;text-align:center; line-height:44px; text-decoration:none; color:white; width:300px; height: 15px; font-weight:bold; font-size:16px; background-color: rgba(24, 164, 189, 0.979); font-familly:'proxima-nova'; padding: 10px 100px; border-radius: 25px; margin-left: 315px;\">Réinitialiser mon mot de passe</a></p>" +
                                        "<br>"+ "<br>"+
                                        "<p style=\"font-size:16px;font-familly:'proxima-nova';\"> <strong>Attention</strong>, ce lien n'est utilisable qu'une seule fois et est valable 24H. </p> " +"<br>"+
                                        "<br>" + "<p style=\"font-size:16px; font-familly:'proxima-nova';\"> <strong>Important:</strong> Votre nouveau mot de passe doit être complexe et contenir 8 caractères dont 1 majuscule, 1 minuscule, un chiffre et un caractère spécifique.</p>" +"<br>"+
                                        "<br>"+"<br>"+
                                        "<br>"+"Nous vous remercions de votre confiance." +"<br>" + "<br>"+
                                        "L'équipe SODENKAMIN SUPPORT" + "<br>" +
                                        "SAS au capital de 20.000 euros" + "<br>"+
                                        "Siège Social: 04 Allée Pauline Isabelle Utiles,44200 Nantes" +  "<br>"+
                                        "Immatriculée sous le numéro: 123456789 -Société de financement agréee et contrôlée par l'ACPR"
                                )
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

    /** methode qui permet de notifier le vendeur du stock insuffisant
     * @param sender           l'emeteur du message
     * @param recipient        le destinataire du mail
     * @param subject          l'objet du mail
     * @param body             le contenu du mail
     * @throws MailjetSocketTimeoutException Exception lié au timeout de la socket
     */
    public void informSellerStokLowest(final String sender,
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
                                        .put("Name", "SodenKamin"))
                                .put(Emailv31.Message.TO, new JSONArray()
                                        .put(new JSONObject()
                                                .put("Email", recipient)))
                                .put(Emailv31.Message.SUBJECT, subject + date.substring(0, 10))
                                .put(Emailv31.Message.TEXTPART, body)
                                .put(Emailv31.Message.HTMLPART,
                                        "<img src=\"https://xt96z.mjt.lu/tplimg/xt96z/b/05890/6xmo3.jpeg\" alt=\"logo\" width=\"30\" height=\"30\" class=\"d-inline-block align-top\">"+
                                                "<span style=\"font-size: 25px; color:red\">SODENKAMIN </span>"
                                                + "<br>" + "<br>"+ "<br>" +
                                                "<p style=\"font-size:16px; font-familly:'proxima-nova';\">"  + body +  "<br>\n" + "<br>\n" +
                                                "<p style=\"font-size:16px;font-familly:'proxima-nova';\"> <strong>Attention</strong>, Veuillez desactiver ou augmenter sa quantité en stock. </p> " +"<br>"+
                                                "<br>"+"Nous vous remercions de votre confiance." +"<br>" + "<br>"+
                                                "L'équipe SODENKAMIN SUPPORT" + "<br>" +
                                                "SAS au capital de 20.000 euros" + "<br>"+
                                                "Siège Social: 04 Allée Pauline Isabelle Utiles,44200 Nantes" +  "<br>"+
                                                "Immatriculée sous le numéro: 123456789 -Société de financement agréee et contrôlée par l'ACPR"
                                )
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