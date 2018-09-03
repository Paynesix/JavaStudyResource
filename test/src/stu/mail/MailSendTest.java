package stu.mail;

import java.util.Properties;

import javax.mail.Authenticator;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

public class MailSendTest {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		final String to = "paynesix@163.com";
		final String from = "Seediqbale_Payne@163.com";
		final String host = "smtp.163.com";
		Properties properties = System.getProperties();
		properties.setProperty("mail.smtp.host", host);
		properties.put("mail.smtp.auth", "true");
//		Session session = Session.getDefaultInstance(properties);
		
		Session session = Session.getDefaultInstance(properties, new Authenticator(){
			public PasswordAuthentication getPasswordAuthentication()
	        {
				return new PasswordAuthentication(from, "mail001"); //发件人邮件用户名、密码
	        }
		});
		
		try {
			MimeMessage message = new MimeMessage(session);
			message.setFrom(new InternetAddress(from));
			message.addRecipient(Message.RecipientType.TO, new InternetAddress(to));
			message.setSubject("Hello Payne, I am your coder");
			message.setText("come on , you are good! ");
			Transport.send(message);
			System.out.print("Send message successfully!");
			
		} catch (AddressException e) {
			e.printStackTrace();
		} catch (MessagingException e) {
			e.printStackTrace();
		}
		
	}

}
