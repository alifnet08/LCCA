package com.wo.module.common.utility;

import java.io.File;
import java.util.List;
import java.util.Properties;

import javax.activation.DataHandler;
import javax.activation.DataSource;
import javax.activation.FileDataSource;
import javax.mail.Message;
import javax.mail.Multipart;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;

import org.apache.commons.lang3.StringUtils;

public class EmailUtil {
	private String smtpServerHost;

	private Integer smtpServerPort;

	private boolean smtpAuth;

	private boolean smtpStartTlsEnable;

	private String defaultEmailUsername;

	private String defaultEmailPassword;

	private String defaultEmail;

	private String defaultEmailTo;

	private String defaultEmailCc;

	private static final String EMAIL_ADDRESS_SEPARATOR = ",";

	public static final String HTML_CONTENT_TYPE = "text/html; charset=UTF-8";

	public EmailUtil(String smtpServerHost, Integer smtpServerPort, boolean smtpAuth, boolean smtpSmartTlsEnable,
			String username, String password, String emailFrom, String emailTo, String emailCc) {
		super();
		this.smtpServerHost = smtpServerHost;
		this.smtpServerPort = smtpServerPort;
		this.smtpAuth = smtpAuth;
		this.smtpStartTlsEnable = smtpSmartTlsEnable;
		this.defaultEmailUsername = username;
		this.defaultEmailPassword = password;
		this.defaultEmail = emailFrom;
		this.defaultEmailTo = emailTo;
		this.defaultEmailCc = emailCc;
	}

	public void sendEmail(String subject, String body, boolean containHtmlTag, List<File> attachmentFiles)
			throws Exception {
		try {
			if (defaultEmailUsername == null || defaultEmailUsername.equals("")) {
				throw new Exception("EmailUtil.send - Email username parameter cannot be null !!");
			}
			if (defaultEmailPassword == null || defaultEmailPassword.equals("")) {
				throw new Exception("EmailUtil.send - Email password parameter cannot be null !!");
			}

			if (defaultEmail == null) {
				throw new Exception("EmailUtil.send - Email from parameter cannot be null !!");
			}

			if (defaultEmailTo == null) {
				throw new Exception("EmailUtil.send - Email to parameter cannot be null !!");
			}

			// Get system properties
			Properties props = System.getProperties();

			// Specify the desired SMTP server
			props.put("mail.smtp.host", this.smtpServerHost);
			if (this.smtpServerPort != null) {
				props.put("mail.smtp.port", this.smtpServerPort);
			}
			// props.put("mail.debug", "true");

			props.put("mail.smtp.auth", this.smtpAuth);
			props.put("mail.smtp.starttls.enable", this.smtpStartTlsEnable);
			// props.put("mail.smtp.ssl.trust", this.smtpServerHost);

			// create a new Session object
			Session session = Session.getInstance(props, new javax.mail.Authenticator() {
				protected PasswordAuthentication getPasswordAuthentication() {
					return new PasswordAuthentication(defaultEmailUsername, defaultEmailPassword);
				}
			});
			
//			Session session = Session.getDefaultInstance(props);
			
			session.setDebug(true);
			// create a new MimeMessage object (using the Session created above)
			Message message = new MimeMessage(session);

			// set from address
			message.setFrom(new InternetAddress(defaultEmail));

			// set sent date
			// message.setSentDate(email.getSentDate() != null ?
			// email.getSentDate() : DateUtil.getCurrentDateFromDb());

			// set recepient address
			if (defaultEmailTo.indexOf(EMAIL_ADDRESS_SEPARATOR) > 0) {
				message.addRecipients(Message.RecipientType.TO, InternetAddress.parse(defaultEmailTo));
			} else {
				message.setRecipient(Message.RecipientType.TO, new InternetAddress(defaultEmailTo));
			}

			if (defaultEmailCc.indexOf(EMAIL_ADDRESS_SEPARATOR) > 0) {
				message.addRecipients(Message.RecipientType.CC, InternetAddress.parse(defaultEmailCc));
			} else {
				if (StringUtils.isNoneEmpty(defaultEmailCc)) {
					message.setRecipient(Message.RecipientType.CC, new InternetAddress(defaultEmailCc));
				}
			}

			// set subject
			message.setSubject(subject);

			// message.setHeader("", "");

			// set message and attachment
			if (attachmentFiles != null && !attachmentFiles.isEmpty()) {

				Multipart multipart = new MimeMultipart();
				MimeBodyPart messageBodyPart = null;

				// part one is message
				messageBodyPart = new MimeBodyPart();
				if (containHtmlTag)
					messageBodyPart.setContent(body, HTML_CONTENT_TYPE);
				else
					messageBodyPart.setText(body);

				multipart.addBodyPart(messageBodyPart);

				// Part two is attachment
				DataSource fileDataSource = null;
				for (File attachmentFile : attachmentFiles) {
					messageBodyPart = new MimeBodyPart();
					fileDataSource = new FileDataSource(attachmentFile.getAbsolutePath());
					messageBodyPart.setDataHandler(new DataHandler(fileDataSource));

					messageBodyPart.setFileName(attachmentFile.getName());
					multipart.addBodyPart(messageBodyPart);
				}

				// put all parts
				message.setContent(multipart);
			} else {
				if (containHtmlTag)
					message.setContent(body, HTML_CONTENT_TYPE);
				else
					message.setText(body);
			}

			Transport.send(message);

		} catch (Exception e) {
			throw new Exception("EmailUtil.send - Subject[" + subject + "] body [" + body + "]" + ", Message : " + e);
		}
	}

	public void sendEmail(String subject, String body, boolean containHtmlTag) throws Exception {
		sendEmail(subject, body, containHtmlTag, null);
	}

	public void sendEmail(String subject, String body) throws Exception {
		sendEmail(subject, body, false, null);
	}

	// Function to insert string
	public static String insertString(String originalString, String stringToBeInserted, int index) {

		// Create a new string
		String newString = new String();

		for (int i = 0; i < originalString.length(); i++) {

			// Insert the original string character
			// into the new string
			newString += originalString.charAt(i);

			if (i == index) {

				// Insert the string to be inserted
				// into the new string
				newString += stringToBeInserted;
			}
		}

		// return the modified String
		return newString;
	}
}