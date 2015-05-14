package cse403.homesafe.Messaging;

import android.content.Context;
import android.content.SharedPreferences;
import android.location.Location;

import android.preference.PreferenceManager;
import android.util.Log;

import cse403.homesafe.Data.Contact;
import cse403.homesafe.Utility.ContextHolder;

import javax.activation.DataHandler;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import javax.mail.util.ByteArrayDataSource;

import java.util.Properties;

/**
 * Abstraction that serves as the interface for sending emails through HomeSafe's
 * email address.
 *
 * Provides functionality for sending an email to a specified Contact given the user's
 * current location, message, and message type.
 */
public class Email extends javax.mail.Authenticator implements cse403.homesafe.Messaging.Message {

    private static final String TAG = "Email";

    private String mailhost = "smtp.gmail.com";
    private String user;
    private String password;
    private Session session;
    private Properties props;

    private static Email _instance;

    // Private constructor which initializes the instance Properties
    // and sets user/password
    private Email(String user, String password) {
        this.user = user;
        this.password = password;

        props = new Properties();
        props.setProperty("mail.transport.protocol", "smtp");
        props.setProperty("mail.host", mailhost);
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.port", "465");
        props.put("mail.smtp.socketFactory.port", "465");
        props.put("mail.smtp.socketFactory.class",
                "javax.net.ssl.SSLSocketFactory");
        props.put("mail.smtp.socketFactory.fallback", "false");
        props.setProperty("mail.smtp.quitwait", "false");

        session = Session.getDefaultInstance(props, this);
    }

    /**
     * Get singleton instance of Email
     *
     * @return Instance of Email
     */
    public static Email getInstance() {
        if (_instance == null)
            _instance = new Email("homesafealerts@gmail.com", "eminatorlak");
        return _instance;
    }

    /**
     * Sends email to recipient with specified Location and specified custom message.
     *
     * @param recipient     Recipient of the intended message
     * @param location      Last location of user
     * @param customMessage Customized message to be sent
     */
    public void sendMessage(final Contact recipient, final Location location, final String customMessage,
                            final Context context, final Messenger.MessageType type) {
        new Thread(new Runnable() {
            public void run() {
                String recipientEmail = recipient.getEmail();

                SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
                String userFirstName = preferences.getString("firstName", null);
                String userLastName = preferences.getString("lastName", null);

                if (userFirstName == null || userLastName == null) {
                    // TODO (Alex): Remove this hacky fix at a later time
                    userFirstName = "";
                    userLastName = "";
                }

                String subject, body;
                if (type == Messenger.MessageType.DANGER) {
                    subject = userFirstName + " " + userLastName + " May Need Your Help";
                    body = userFirstName + " was using HomeSafe, a walking safety app.\n\nThey were"
                            + " using the app to get to a destination, but did not check in with the app."
                            + " As a result, this automated email is being sent to all of " + userFirstName
                            + "'s contacts. Their last known coordinates are (" + location.getLatitude() + ", "
                            + location.getLongitude() + "). You may need to check in with " + userFirstName + "."
                            + "\n\n" + userFirstName + " says: " + customMessage;
                } else {
                    subject = userFirstName + " " + userLastName + " Arrived Safely";
                    body = userFirstName + " was using HomeSafe, a walking safety app.\n\nThey were"
                            + " using the app to get to a destination and they arrived safely. This is"
                            + " an automated message sent by " + userFirstName + "'s phone to"
                            + " notify you of their safe arrival";
                }
                try {
                    sendMail(subject, body, "homesafealerts@gmail.com", recipientEmail);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    // Authentication for email
    protected PasswordAuthentication getPasswordAuthentication() {
        return new PasswordAuthentication(user, password);
    }

    private synchronized void sendMail(String subject, String body, String sender, String recipients) throws Exception {
        try {
            MimeMessage message = new MimeMessage(session);
            DataHandler handler = new DataHandler(new ByteArrayDataSource(body.getBytes(), "text/plain"));
            message.setSender(new InternetAddress(sender));
            message.setSubject(subject);
            message.setDataHandler(handler);
            if (recipients.indexOf(',') > 0)
                message.setRecipients(javax.mail.Message.RecipientType.TO, InternetAddress.parse(recipients));
            else
                message.setRecipient(javax.mail.Message.RecipientType.TO, new InternetAddress(recipients));
            Transport.send(message);
        } catch (MessagingException e) {
            Log.e(TAG, "Unable to send message.");
        }
    }


}

