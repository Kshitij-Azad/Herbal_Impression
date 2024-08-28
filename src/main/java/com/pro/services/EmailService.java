package com.pro.services;

import java.util.Properties;
import javax.mail.Authenticator;
import javax.mail.Message;
import javax.mail.Message.RecipientType;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import org.springframework.stereotype.Service;


@Service
public class EmailService {
	
	public boolean sendEmail(String message,String subject,String to)
	{
		boolean f = false;
		//variable for gmail
		String host = "smtp.gmail.com";
		String from = "mpgaming773@gmail.com";
		
		//get the system properties
		Properties properties = System.getProperties();
		System.out.println("PROPERTIES "+properties);
		
		//setting important information to properties object
		properties.put("mail.smtp.host", host);
		properties.put("mail.smtp.port", "465");
		properties.put("mail.smtp.ssl.enable","true");
		properties.put("mail.smtp.auth","true");
		
		//Step 1 to get the session object...
		Session session= Session.getInstance(properties,new Authenticator() {
			@Override
			protected PasswordAuthentication getPasswordAuthentication()
			{
				return new PasswordAuthentication("mpgaming773@gmail.com","xsnw ehmr mgib razd");
			}
		});
		session.setDebug(true);
		
		//Setp2 compose the message [text,multi media]
		MimeMessage m = new MimeMessage(session);
		
		try {

			// from email
			m.setFrom(from);
			
			//adding recipient to message
			m.addRecipient(Message.RecipientType.TO, new InternetAddress(to));
			
			//addming subject to message
			m.setSubject(subject);
			
			//adding text to message
			m.setText(message);
			
			//send 
			
			//step3 :send the message using transport class
			Transport.send(m);
			System.out.println("Sent Sucess.........");
			f=true;
			
		} 
		catch (Exception e) {
			e.printStackTrace();
		}
		return f;
		
		
	}

}
