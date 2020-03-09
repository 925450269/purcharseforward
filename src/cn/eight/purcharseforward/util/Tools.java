package cn.eight.purcharseforward.util;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class Tools
{
	public static String md5(String str){
		MessageDigest md5=null;
		try {
			md5 = MessageDigest.getInstance("MD5");
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		}
		sun.misc.BASE64Encoder encoder =  new sun.misc.BASE64Encoder();
		String security_str = null;
		try {
			security_str = encoder.encode(md5.digest(str.getBytes("utf-8")));
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		return security_str;
    }
}
