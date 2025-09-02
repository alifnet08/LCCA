package com.wo.module.common.util;

import java.io.Serializable;

import org.apache.commons.lang3.RandomStringUtils;

import com.wo.module.common.utility.MD5Utils;

public class PasswordUtil implements Serializable{
	
	private static final long serialVersionUID = -6277817213962019898L;
	private final static String characters = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789~`!@#$%^&*()-_=+[{]}\\|;:\'\",<.>/?";
	private String randPass;
	
	public String generateRandomPassword() {
		return RandomStringUtils.random( 5 , characters );
	}
	
	public String generateRandomPasswordBase64_md5() {
		String passMd5 = MD5Utils.b64_md5(RandomStringUtils.random( 5 , characters ));
		return passMd5.substring(0,passMd5.length()-2);
	}
	
	public String generateRandomPasswordBase64_md5(String randPass) {
		String passMd5 = MD5Utils.b64_md5(randPass);
		return passMd5.substring(0,passMd5.length()-2)+"==";
	}

	public String getRandPass() {
		return randPass;
	}

	public void setRandPass(String randPass) {
		this.randPass = randPass;
	}
}
