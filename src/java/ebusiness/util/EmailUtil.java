package ebusiness.util;

import jakarta.mail.Message;
import jakarta.mail.MessagingException;
import jakarta.mail.Session;
import jakarta.mail.Transport;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeMessage;
import java.util.Date;
import java.util.Properties;

public class EmailUtil {

    private static final String SMTP_HOST = "localhost";
    private static final int SMTP_PORT = 2525;
    private static final String FROM_EMAIL = "CENTRE@glassfish.com";

    public static void sendCode(String toEmail, String subject, String label, String code) throws MessagingException {
        // FakeSMTP listens locally during development, so no authentication is configured here.
        Properties props = new Properties();
        props.put("mail.smtp.host", SMTP_HOST);
        props.put("mail.smtp.port", String.valueOf(SMTP_PORT));

        Session session = Session.getDefaultInstance(props);
        MimeMessage message = new MimeMessage(session);
        message.setFrom(new InternetAddress(FROM_EMAIL));
        message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(toEmail));
        message.setSubject(subject);
        message.setSentDate(new Date());
        message.setText(label + ": " + code);
        Transport.send(message);
    }
}
