package com.imooc.miaosha.utils;

//import org.apache.commons.codec.digest.DigestUtils;
import org.springframework.util.DigestUtils;

import java.nio.charset.StandardCharsets;

/**
 * 用户密码加密
 */
public class MD5Util {

	private static final String salt = "1a2b3c4d"; 	// 用于 MD5 加密的盐值，与原字符串拼接进行加密

	/**
	 * md5 加密
	 * @param src
	 * @return
	 */
	public static String md5(String src) {
		return DigestUtils.md5DigestAsHex(src.getBytes(StandardCharsets.UTF_8));
//				.md5Hex(src);
	}

	/**
	 * md5(原密码 + salt)
	 * @param inputPass
	 * @return
	 */
	public static String inputPassToFormPass(String inputPass) {
		String str = ""+salt.charAt(0)+salt.charAt(2) + inputPass +salt.charAt(5) + salt.charAt(4);
//		System.out.println(str);
		return md5(str);
	}

	/**
	 * md5(密码 + salt)
	 * @param formPass
	 * @param salt
	 * @return
	 */
	public static String formPassToDBPass(String formPass, String salt) {
		String str = ""+salt.charAt(0)+salt.charAt(2) + formPass +salt.charAt(5) + salt.charAt(4);
		return md5(str);
	}

	/**
	 * md5(md5(原密码 + salt) + salt)
	 * @param inputPass
	 * @param saltDB
	 * @return 最终保存在数据库的密码字符串
	 */
	public static String inputPassToDbPass(String inputPass, String saltDB) {
		String formPass = inputPassToFormPass(inputPass);
		String dbPass = formPassToDBPass(formPass, saltDB);
		return dbPass;
	}
	
	public static void main(String[] args) {
//		System.out.println(inputPassToFormPass("123456"));//d3b1294a61a07da9b49b6e22b2cbd7f9
////		System.out.println(formPassToDBPass(inputPassToFormPass("123456"), "1a2b3c4d"));
		System.out.println(inputPassToDbPass("123456", "1a2b3c4d"));//b7797cce01b4b131b433b6acf4add449
	}
	
}
