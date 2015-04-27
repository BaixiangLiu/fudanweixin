package edu.fudan.eservice.common.utils;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.security.GeneralSecurityException;
import java.security.InvalidKeyException;
import java.security.Key;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.spec.AlgorithmParameterSpec;
import java.util.Random;

import javax.crypto.Cipher;
import javax.crypto.Mac;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

import org.apache.commons.codec.binary.Base64;



/**
 * 哈希与对称加解密工具
 * 
 * @author wking
 * 
 */

public class EncodeHelper {

	/**
	 * 哈希算法
	 * 
	 * @param orgin
	 *            源
	 * @param algorithm
	 *            算法
	 * @return
	 */
	public static String digest(String orgin, String algorithm) {

		try {
			byte[] strTemp = orgin.getBytes("utf-8");
			MessageDigest mdTemp = MessageDigest.getInstance(algorithm);
			mdTemp.update(strTemp);
			byte[] md = mdTemp.digest();

			return bytes2hex(md);
		} catch (Exception e) {
			return null;
		}
	}

	public static String bytes2hex(byte[] bts) {
		char hexDigits[] = { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f' };
		int j = bts.length;
		char str[] = new char[j * 2];
		int k = 0;
		for (int i = 0; i < j; i++) {
			byte byte0 = bts[i];
			str[k++] = hexDigits[byte0 >>> 4 & 0xf];
			str[k++] = hexDigits[byte0 & 0xf];
		}
		return new String(str);
	}

	public static byte[] hex2bytes(String hex) {
		int i = hex.length() / 2;
		byte abyte0[] = new byte[i];
		for (int j = 0; j < i; j++) {
			String s1 = hex.substring(j * 2, j * 2 + 2);
			abyte0[j] = (byte) Integer.parseInt(s1, 16);
		}

		return abyte0;
	}

	/**
	 * 生成随机字符串
	 * 
	 * @param length
	 * @return
	 */
	public static String randpass(int length) {
		StringBuffer s = new StringBuffer();
		Random r = new Random();
		for (int i = 0; i < length; i++) {
			s.append((char) (r.nextInt(94) + 33));
		}
		return s.toString();
	}

	/**
	 * 
	* @Title: MD5
	* @Description: md5加密
	* @param s
	* @return
	 */
	public final static String MD5(byte[] message) {

		try {
			// 获得MD5摘要算法的 MessageDigest 对象
			MessageDigest mdInst = MessageDigest.getInstance("MD5");
			// 使用指定的字节更新摘要
			mdInst.update(message);
			// 获得密文
			return bytes2hex(mdInst.digest());
			// 把密文转换成十六进制的字符串形式
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}


	/**
	 * 对称加密
	 * 
	 * @param algorithm
	 *            算法
	 * @param src
	 *            明文
	 * @param key
	 *            密钥
	 * @param vector
	 *            密钥空间向量
	 * @return
	 * @throws GeneralSecurityException
	 */
	public static byte[] encrypt(String algorithm, byte[] src, byte[] key, byte[] vector) throws GeneralSecurityException {
		// DESKeySpec dks=new DESKeySpec(key);
		// SecretKeyFactory factory=SecretKeyFactory.getInstance(algorithm);
		// SecretKey skey=factory.generateSecret(dks);
		Key skey = new SecretKeySpec(key, algorithm);
		Cipher ciper = null;
		if (vector != null) {
			AlgorithmParameterSpec iv = new IvParameterSpec(vector);
			ciper = Cipher.getInstance(algorithm + "/CBC/PKCS5Padding");
			ciper.init(Cipher.ENCRYPT_MODE, skey, iv);
		} else {
			ciper = Cipher.getInstance(algorithm + "/ECB/PKCS5Padding");
			ciper.init(Cipher.ENCRYPT_MODE, skey);
		}
		return ciper.doFinal(src);

	}

	/**
	 * 对称解密
	 * 
	 * @param algorithm
	 *            算法
	 * @param src
	 *            密文
	 * @param key
	 *            密钥
	 * @param vector
	 *            密钥向量空间
	 * @return
	 * @throws GeneralSecurityException
	 */
	public static byte[] dencrypt(String algorithm, byte[] src, byte[] key, byte[] vector) throws GeneralSecurityException {
		// DESKeySpec dks=new DESKeySpec(key);
		// SecretKeyFactory factory=SecretKeyFactory.getInstance(algorithm);
		// SecretKey skey=factory.generateSecret(dks);
		Key skey = new SecretKeySpec(key, algorithm);
		Cipher ciper = null;
		if (vector != null) {
			AlgorithmParameterSpec iv = new IvParameterSpec(vector);
			ciper = Cipher.getInstance(algorithm + "/CBC/PKCS5Padding");
			ciper.init(Cipher.DECRYPT_MODE, skey, iv);
		} else {
			ciper = Cipher.getInstance(algorithm + "/ECB/PKCS5Padding");
			ciper.init(Cipher.DECRYPT_MODE, skey);
		}
		return ciper.doFinal(src);

	}

	/**
	 * 解码
	 * 
	 * @param source
	 * @param type
	 *            BASE64或URL
	 * @return
	 */
	public static String decode(String source, String type) {
		if ("BASE64".equalsIgnoreCase(type))
			try {
				return new String(Base64.decodeBase64(source), "utf-8");
			} catch (IOException e) {

				return "";
			}
		if ("URL".equalsIgnoreCase(type))
			try {
				return URLDecoder.decode(source, "utf-8");
			} catch (UnsupportedEncodingException e) {
				return "";
			}
		return "";
	}

	/**
	 * 编码
	 * 
	 * @param source
	 * @param type
	 *            BASE64或URL
	 * @return
	 */
	public static String encode(String source, String type) {
		if ("BASE64".equalsIgnoreCase(type))
			try {
				return new Base64().encodeToString(source.getBytes("utf-8"));
			} catch (IOException e) {

				return "";
			}
		if ("URL".equalsIgnoreCase(type))
			try {
				return URLEncoder.encode(source, "utf-8");
			} catch (UnsupportedEncodingException e) {
				return "";
			}
		return "";
	}

	/**
	 * 生成签名
	 * @param hash
	 * @param data
	 * @param key
	 * @return
	 */
	public static String hmac(String hash,String data, String key) {
		byte[] byteHMAC = null;
		try {
			Mac mac = Mac.getInstance("Hmac"+hash);
			SecretKeySpec spec = new SecretKeySpec(key.getBytes(), "Hmac"+hash);
			mac.init(spec);
			byteHMAC = mac.doFinal(data.getBytes("UTF-8"));
		} catch (InvalidKeyException e) {
			e.printStackTrace();
		} catch (NoSuchAlgorithmException ignore) {
			ignore.printStackTrace();
		} catch (IllegalStateException e) {
	    e.printStackTrace();
    } catch (UnsupportedEncodingException e) {
	    e.printStackTrace();
    }

		if (byteHMAC != null) {
			try {
				String hexMac = bytes2hex(byteHMAC);
				return hexMac;
			} catch (Exception e) {
				e.printStackTrace();
			}
			return null;
		}
		return null;
	}
	
}
