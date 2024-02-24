package com.agroprod.util;

import java.io.File;

import javax.mail.Message;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.FileSystemResource;
import org.springframework.mail.MailException;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.mail.javamail.MimeMessagePreparator;
import org.springframework.stereotype.Component;

@Component
public class SendMail {
	@Autowired
	private JavaMailSender mailSender;

	public void sendItinerary(String to, String subject, String folderPath, String fileName, String customerName) {
		MimeMessagePreparator preparator = new MimeMessagePreparator() {

			public void prepare(MimeMessage mimeMessage) throws Exception {

				mimeMessage.setRecipient(Message.RecipientType.TO, new InternetAddress(to));
				mimeMessage.setFrom(new InternetAddress("rakeshdemopriya@gmail.com"));
				mimeMessage.setSubject(subject);

				String msg = "Dear " + customerName + "," + System.getProperty("line.separator")
						+ System.getProperty("line.separator")
						+ "Payment against the product has been received successfully. PFA the invoice for the order. Feel free to contact seller if you have any further questions. For better experience please visit our AgroProduct App again in the future."
						+ System.getProperty("line.separator") + System.getProperty("line.separator") + "Thank You"
						+ System.getProperty("line.separator") + "AgroProduct Team ";

				MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true);

				helper.setText("<html><body><img src='cid:folderPath'></body></html>", true);

				helper.setText(msg);

				FileSystemResource res = new FileSystemResource(new File(folderPath + ".pdf"));

				helper.addAttachment(fileName, res);

			}
		};

		try {
			mailSender.send(preparator);
		} catch (MailException ex) {
			System.err.println(ex.getMessage());
		}
	}
}