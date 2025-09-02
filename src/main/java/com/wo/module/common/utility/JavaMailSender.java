package com.wo.module.common.utility;

public class JavaMailSender extends org.springframework.mail.javamail.JavaMailSenderImpl {

	
	
	public JavaMailSender(String host,String userName,String password,String protocol){
		
		super.setHost(host);
		super.setUsername(userName);
		super.setPassword(password);
		super.setProtocol(protocol);
		
	}

	
	
	
}