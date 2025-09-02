package com.wo.module.common.util;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.Base64;

import org.apache.commons.lang3.StringUtils;

//import sun.misc.BASE64Decoder;
//import sun.misc.BASE64Encoder;

public class EncryptUtils {
	public static final String DEFAULT_ENCODING = "UTF-8";
	//static BASE64Encoder enc = new BASE64Encoder();
	//static BASE64Decoder dec = new BASE64Decoder();
	
	private static final String DEFAULT_KEY = "In!Ad@l4hB3ntuKP312janJi4n42taRAW0tM4y1342K1nd0N3$*#";

	public static String base64encode(String text) {
		try {
			//return enc.encode(text.getBytes(DEFAULT_ENCODING));
			return Base64.getEncoder().encodeToString(text.getBytes(DEFAULT_ENCODING));
		} catch (UnsupportedEncodingException e) {
			return null;
		}
	}// base64encode

	public static String base64decode(String text) {
		try {
			//return new String(dec.decodeBuffer(text), DEFAULT_ENCODING);
			return new String(Base64.getDecoder().decode(text),DEFAULT_ENCODING);
		} catch (Exception e) {
			return null;
		}
	}// base64decode

//	public static void main(String[] args) {
//		String txt = "some text to be encrypted";
//		String key = "key phrase used for XOR-ing";
//		System.out.println(txt + " XOR-ed to: " + (txt = xorMessage(txt, null)));
//
//		String encoded = base64encode(txt);
//		System.out.println(" is encoded to: " + encoded + " and that is decoding to: " + (txt = base64decode(encoded)));
//		System.out.print("XOR-ing back to original: " + xorMessage(txt, null));
//	}

	public static String xorMessage(String message, String key) {
		if (StringUtils.isBlank(key)) {
			key = DEFAULT_KEY;
		}
		try {
			if (message == null || key == null)
				return null;

			char[] keys = key.toCharArray();
			char[] mesg = message.toCharArray();

			int ml = mesg.length;
			int kl = keys.length;
			char[] newmsg = new char[ml];

			for (int i = 0; i < ml; i++) {
				newmsg[i] = (char) (mesg[i] ^ keys[i % kl]);
			} // for i

			return new String(newmsg);
		} catch (Exception e) {
			return null;
		}
	}// xorMessage
}// class