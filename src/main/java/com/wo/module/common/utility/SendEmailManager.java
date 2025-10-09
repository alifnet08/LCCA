package com.wo.module.common.utility;

import javax.mail.internet.MimeMessage;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.mail.MailSendException;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.mail.javamail.MimeMessagePreparator;

import com.wo.module.common.constant.Constants;
import com.wo.module.common.model.EmailNotification;

public class SendEmailManager {

	private static final Logger logger = LoggerFactory.getLogger(SendEmailManager.class);

	private JavaMailSenderImpl mailSender;

	private String emailAddressFrom;
	private String emailTo;
	private String emailDomain;

	public JavaMailSender javaMailSender() {
		JavaMailSenderImpl mailSender = new JavaMailSenderImpl();

		/*
		 * Properties mailProperties = new Properties();
		 * mailProperties.put("mail.smtp.auth", systemAuth.getValue());
		 * mailProperties.put("mail.smtp.starttls.enable", "true");
		 * mailProperties.put("mail.smtp.starttls.required", "true");
		 * mailProperties.put("mail.smtp.socketFactory.port", systemPort.getValue());
		 * mailProperties.put("mail.smtp.debug", "true");
		 * mailProperties.put("mail.smtp.socketFactory.class",
		 * "javax.net.ssl.SSLSocketFactory");
		 * mailProperties.put("mail.smtp.socketFactory.fallback", "false");
		 * 
		 * mailSender.setJavaMailProperties(mailProperties);
		 * mailSender.setHost(systemHost.getValue());
		 * mailSender.setPort(Integer.parseInt(systemPort.getValue()));
		 * mailSender.setProtocol(systemProtocol.getValue());
		 * mailSender.setUsername(systemUserName.getValue());
		 * mailSender.setPassword(systemPassword.getValue());
		 */
		return mailSender;
	}

	public String getEmailAddressFrom() {
		return emailAddressFrom;
	}

	public void setEmailAddressFrom(String emailAddressFrom) {
		this.emailAddressFrom = emailAddressFrom;
	}

	public String getEmailTo() {
		return emailTo;
	}

	public void setEmailTo(String emailTo) {
		this.emailTo = emailTo;
	}

	public String getEmailDomain() {
		return emailDomain;
	}

	public void setEmailDomain(String emailDomain) {
		this.emailDomain = emailDomain;
	}

	@SuppressWarnings("unused")
	public void sendEmail(final String emailAddressTo, final String subject, final String content) {

		/*
		 * System systemHost = systemService.findByCode("MAIL_SMTP_HOST"); System
		 * systemPort = systemService.findByCode("MAIL_SMTP_PORT"); final System
		 * systemUserName = systemService.findByCode("MAIL_SMTP_USER"); System
		 * systemPassword = systemService.findByCode("MAIL_SMTP_PASSWORD"); System
		 * systemProtocol = systemService.findByCode("MAIL_TRANSPORT_PROTOCOL"); System
		 * systemAuth = systemService.findByCode("MAIL_SMTP_AUTH");
		 * 
		 * emailAddressFrom = systemUserName.getValue();
		 */

		EmailNotification en = new EmailNotification();
		en.setEmailTo(emailAddressTo);
		en.setSubject(subject);
		en.setMessage(content);

		MimeMessagePreparator preparator = new MimeMessagePreparator() {
			public void prepare(MimeMessage mimeMessage) throws Exception {
				MimeMessageHelper message = new MimeMessageHelper(mimeMessage);
				message.setFrom(emailAddressFrom);
				message.setTo(emailAddressTo);
				message.setSubject(subject);
				message.setText(content, true);
			}
		};
		try {
			// this.mailSender.setJavaMailProperties(mailProperties);
			/*
			 * this.mailSender.setHost(systemHost.getValue());
			 * this.mailSender.setPort(Integer.parseInt(systemPort.getValue()));
			 * this.mailSender.setUsername(systemUserName.getValue());
			 * this.mailSender.setPassword(systemPassword.getValue());
			 * this.mailSender.setProtocol(systemProtocol.getValue());
			 * this.mailSender.getJavaMailProperties().setProperty("mail.smtp.auth",
			 * systemAuth.getValue()); this.mailSender.send(preparator);
			 */

			en.setStatus(Constants.EMAIL_SUCCESS);
		} catch (MailSendException send) { // I used MailException as well
			// System.out.println("err=="+send);
			String result = send.getMessage();
			if (result != null && result.length() > 99) {
				en.setStatus(result.substring(0, 99));
			} else {
				en.setStatus(result);
			}
		}

	}

	public void sendEmail1(String subject, String body, String emailAddressTo) {
		/*
		 * System systemHost = systemService.findByCode("MAIL_SMTP_HOST"); System
		 * systemPort = systemService.findByCode("MAIL_SMTP_PORT"); System
		 * systemUserName = systemService.findByCode("MAIL_SMTP_USER"); System
		 * systemPassword = systemService.findByCode("MAIL_SMTP_PASSWORD");
		 * 
		 * System systemProtocol = systemService.findByCode("MAIL_TRANSPORT_PROTOCOL");
		 * 
		 * System systemAuth = systemService.findByCode("MAIL_SMTP_AUTH"); System
		 * smtpSmartTlsEnable = systemService.findByCode("MAIL_SMTP_STARTTLS_ENABLE");
		 * String emailFrom = "xxxxx@btpnsyariah.com";
		 * 
		 * EmailUtil emailUtil = new EmailUtil(systemHost.getValue(),
		 * Integer.valueOf(systemPort.getValue()),
		 * Boolean.valueOf(systemAuth.getValue()),
		 * Boolean.valueOf(smtpSmartTlsEnable.getValue()), systemUserName.getValue(),
		 * systemPassword.getValue(), emailFrom, emailAddressTo);
		 */
		try {
			logger.info("Send email activation success");
			java.lang.System.out.println("Send email activation success");
			//emailUtil.sendEmail(subject, body);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public JavaMailSenderImpl getMailSender() {
		return mailSender;
	}

	public void setMailSender(JavaMailSenderImpl mailSender) {
		this.mailSender = mailSender;
	}

}
