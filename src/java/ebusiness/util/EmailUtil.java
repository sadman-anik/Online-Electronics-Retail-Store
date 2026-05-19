package ebusiness.util;

import jakarta.mail.Message;
import jakarta.mail.MessagingException;
import jakarta.mail.Session;
import jakarta.mail.Transport;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeMessage;
import java.net.ConnectException;
import java.net.NoRouteToHostException;
import java.net.UnknownHostException;
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

    public static String describeSendFailure(MessagingException ex) {
        if (hasCause(ex, ConnectException.class)) {
            return "Email server is down or not running. Please start FakeSMTP on localhost port 2525, then try again.";
        }
        if (hasCause(ex, UnknownHostException.class) || hasCause(ex, NoRouteToHostException.class)) {
            return "Email server cannot be reached. Check the SMTP host and network connection, then try again.";
        }
        return "Unable to send email: " + rootMessage(ex);
    }

    private static boolean hasCause(Throwable throwable, Class<? extends Throwable> type) {
        while (throwable != null) {
            if (type.isInstance(throwable)) {
                return true;
            }
            if (throwable instanceof MessagingException) {
                Exception next = ((MessagingException) throwable).getNextException();
                if (next != null && next != throwable.getCause()) {
                    if (hasCause(next, type)) {
                        return true;
                    }
                }
            }
            throwable = throwable.getCause();
        }
        return false;
    }

    private static String rootMessage(Throwable throwable) {
        String message = throwable.getMessage();
        while (throwable != null) {
            if (throwable.getMessage() != null && !throwable.getMessage().trim().isEmpty()) {
                message = throwable.getMessage();
            }
            if (throwable instanceof MessagingException) {
                Exception next = ((MessagingException) throwable).getNextException();
                if (next != null && next != throwable.getCause()) {
                    String nextMessage = rootMessage(next);
                    if (nextMessage != null && !nextMessage.trim().isEmpty()) {
                        return nextMessage;
                    }
                }
            }
            throwable = throwable.getCause();
        }
        return message == null || message.trim().isEmpty() ? "Unknown email error." : message;
    }
}
