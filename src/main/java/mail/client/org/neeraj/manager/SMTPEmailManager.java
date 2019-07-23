package mail.client.org.neeraj.manager;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Properties;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import mail.client.org.neeraj.client.EmailConfiguration;

public class SMTPEmailManager implements EmailManager {
	private Properties emailprop;
	private EmailConfiguration configuration;
	private String emailFrom;
	public SMTPEmailManager(String mailConfig) {
		try {
			emailprop=new Properties();
			emailprop.load(new FileInputStream(mailConfig));
			emailFrom=emailprop.getProperty("mail.smtp.email");
			configuration=new EmailConfiguration(
					emailprop.getProperty("mail.smtp.host"),
					emailprop.getProperty("mail.smtp.username"),
					emailprop.getProperty("mail.smtp.password"));
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		
	}
	
	private Session connect(){
		// Session mailSession = Session.getDefaultInstance(emailprop, null);
		Session mailSession = Session.getInstance(emailprop, 
		          new javax.mail.Authenticator() { 
		            //override the getPasswordAuthentication method 
		            protected PasswordAuthentication  
		                           getPasswordAuthentication() { 
		                return new PasswordAuthentication(emailprop.getProperty("mail.smtp.username"),
		    					emailprop.getProperty("mail.smtp.password")); 
		            } 
		          }); 
		return mailSession;
	}

	@Override
	public void sendMessage(String recipient, String subject, String content) {
		String[] toEmails = { recipient };
		Session mailSession= connect();
		MimeMessage emailMessage = new MimeMessage(mailSession);
		try {

			for (int i = 0; i < toEmails.length; i++) {
				emailMessage.addRecipient(Message.RecipientType.TO, new InternetAddress(toEmails[i]));
			}

			emailMessage.setSubject(subject);
			emailMessage.setContent(content, "text/html");
			emailMessage.setFrom(new InternetAddress(emailFrom)); 
			synchronized (this) {
				Transport transport = mailSession.getTransport("smtp");
				transport.connect(configuration.getHostname(), configuration.getUsername(), configuration.getPassword());
				transport.sendMessage(emailMessage, emailMessage.getAllRecipients());
				transport.close();
			}
		} catch (MessagingException e) {
			e.printStackTrace();
		}

	}

	@Override
	public void readMessage() {
		// TODO Auto-generated method stub
		
	}

}
