package cse403.homesafe.Messaging;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.location.Location;
import android.net.Uri;
import android.os.IBinder;
import android.preference.PreferenceManager;

import cse403.homesafe.Data.Contact;
import cse403.homesafe.Utility.ContextHolder;

import javax.activation.DataHandler;
import javax.activation.DataSource;
import javax.mail.PasswordAuthentication;
import javax.mail.Message;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import javax.mail.util.ByteArrayDataSource;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.security.AccessController;
import java.security.Security;
import java.util.Properties;

import java.security.AccessController;
import java.security.Provider;

/**
 * Abstraction that serves as the interface for sending emails through TODO: Decide email service
 */
public class Email extends javax.mail.Authenticator implements cse403.homesafe.Messaging.Message {

    private String mailhost = "smtp.gmail.com";
    private String user;
    private String password;
    private Session session;
    private Properties props;

    private static Email _instance;

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
     * @return Instance of Email
     */
    public static Email getInstance() {
        if (_instance == null)
            _instance = new Email("homesafealerts@gmail.com", "eminatorlak");
        return _instance;
    }

    /**
     * Sends email to recipient
     * @param recipient     Recipient of the intended message
     * @param location      Last location of user
     * @param customMessage Customized message to be sent
     */
    public void sendMessage(Contact recipient, Location location, String customMessage) {
        String recipientEmail = recipient.getEmail();

        Context currentContext = ContextHolder.getContext();

//        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(currentContext);
//        String userFirstName = preferences.getString("firstName", null);
//        String userLastName = preferences.getString("lastName", null);

        String userFirstName = "Joe";
        String userLastName = "Doe";

//        if (userFirstName == null || userLastName == null)
//            throw new RuntimeException("User's first or last name is missing.");


        String subject = userFirstName + " " + userLastName + " May Need Your Help";
        String body = userFirstName + " was using HomeSafe, a walking safety app.\n\n They were"
                + " using the app to get to a destination, but did not check in with the app."
                + " As a result, this is an automated email being sent to all of " + userFirstName
                + "'s contacts. Their last know location is (" + location.getLatitude() + ", "
                + location.getLongitude() + "). You may need to check in with " + userFirstName + "."
                + "\n\n" + userFirstName + " says: " + customMessage;

        try {
            sendMail(subject, body, "homesafealerts@gmail.com", recipientEmail);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    protected PasswordAuthentication getPasswordAuthentication() {
        return new PasswordAuthentication(user, password);
    }

    private synchronized void sendMail(String subject, String body, String sender, String recipients) throws Exception {
        try{
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
        }catch(Exception e){

        }
    }


}

