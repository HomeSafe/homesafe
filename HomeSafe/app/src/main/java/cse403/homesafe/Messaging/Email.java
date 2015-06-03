package cse403.homesafe.Messaging;

import android.content.Context;
import android.content.SharedPreferences;
import android.location.Location;

import android.preference.PreferenceManager;
import android.text.Html;
import android.text.TextUtils;
import android.util.Log;

import cse403.homesafe.Data.Contact;

import javax.activation.DataHandler;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import javax.mail.util.ByteArrayDataSource;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import static cse403.homesafe.Messaging.Messenger.*;

/**
 * Abstraction that serves as the interface for sending emails through HomeSafe's
 * email address.
 *
 * Provides functionality for sending an email to a specified Contact given the user's
 * current location, message, and message type.
 */
public class Email extends javax.mail.Authenticator implements Message {

    private static final String TAG = "Email";

    private String user;
    private String password;
    private String mailHost;
    private Session session;
    private Properties props;

    private static Email _instance;

    // Private constructor which initializes the instance Properties
    // and sets user/password
    private Email(String user, String password) {
        this.user = user;
        this.password = password;
        mailHost = "smtp.gmail.com";

        props = new Properties();
        props.setProperty("mail.transport.protocol", "smtp");
        props.setProperty("mail.host", mailHost);
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
    @Override
    public void sendMessage(final Contact recipient, final Location location, final String customMessage,
                            final Context context, final MessageType type) {

        (new Thread() {

            public void run() {
                String recipientEmail = recipient.getEmail();

                SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
                String userFirstName = preferences.getString("firstName", null);
                String userLastName = preferences.getString("lastName", null);

                if (userFirstName == null)
                    userFirstName = "";
                if (userLastName == null)
                    userLastName = "";

                String subject, body;
                body =

                        buildBody(type, context, userFirstName, userLastName, customMessage, location);

                subject = userFirstName + " " + userLastName;
                subject += (type == MessageType.DANGER) ? " May Need Your Help" : " Arrived Safely";

                try

                {
                    sendMail(subject, body, "homesafealerts@gmail.com", recipientEmail);
                } catch (
                        Exception e
                        )

                {
                    e.printStackTrace();
                }
            }
        }).start();

    }

    /**
     * combine all of the information and build body of html page
     * @param type  HOMESAFE or DANGER
     * @param context   application context
     * @param first     first name of user
     * @param last      last name of user
     * @param message   custom message from user
     * @param location  last known location if type is DANGER
     * @return  the body of html page
     */
    private String buildBody(MessageType type, Context context, String first, String last, String message, Location location) {
        try {
            InputStream is = context.getAssets().open("Prefix.html");
            int size = is.available();
            byte[] buffer = new byte[size];
            is.read(buffer);
            is.close();
            String prefix = new String(buffer);

            if (type == MessageType.HOMESAFE)
                is = context.getAssets().open("ArrivalFormat.html");
            else
                is = context.getAssets().open("DangerFormat.html");

            size = is.available();
            buffer = new byte[size];
            is.read(buffer);
            is.close();
            String optional = (location == null) ? " Note: unknown last location. Link above will not work." : "";
            location = (location == null) ? new Location("") : location;
            String format;
            if (type == MessageType.HOMESAFE)
                format = String.format(new String(buffer), first, last, first, first);
            else
                format = String.format(new String(buffer), first, last, first, first, location.getLatitude(), location.getLongitude(),
                        location.getLatitude(), location.getLongitude(), first, first, optional, message);

            is = context.getAssets().open("Suffix.html");
            size = is.available();
            buffer = new byte[size];
            is.read(buffer);
            is.close();
            String suffix = new String(buffer);

            return prefix + format + suffix;

        } catch (IOException e) {
            Log.e(TAG, "Unable to open file");
        }
        return null;
    }

    // Authentication for email
    @Override
    protected PasswordAuthentication getPasswordAuthentication() {
        return new PasswordAuthentication(user, password);
    }

    /**
     * send out the email
     * @param subject   subject of html
     * @param body      body of html
     * @param sender    email of homeSafe group
     * @param recipients    email of emergency contacts
     * @throws MessagingException if unable to send message
     */
    private synchronized void sendMail(String subject, String body, String sender, String recipients) throws Exception {
        try {
            MimeMessage message = new MimeMessage(session);
            DataHandler handler = new DataHandler(new ByteArrayDataSource(body.getBytes(), "text/html"));
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

