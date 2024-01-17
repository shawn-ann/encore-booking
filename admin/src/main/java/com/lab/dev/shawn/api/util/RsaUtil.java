package com.lab.dev.shawn.api.util;


import org.apache.commons.codec.binary.Base64;

import javax.crypto.Cipher;
import java.security.*;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;


public class RsaUtil {

	private static final String KEY_ALGORITHM = "RSA";
	private static final String ALGORITHM_MD5RSA = "MD5WithRSA";

	static {
		try {
			java.security.Security.addProvider(new org.bouncycastle.jce.provider.BouncyCastleProvider());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * 私钥签名
	 */
	public static String sign2Base64ByMd5Rsa(String base64PriRsa, String inputStr,String charset) {
		return sign2Base64ByAlg(base64PriRsa, inputStr, ALGORITHM_MD5RSA,charset);
	}
	
	/**
	 * 公钥验签
	 */
	public static boolean verifySignByMd5Rsa(String base64PubRsa, String src, String base64SignValue,String charset) {
		return verifySignByAlg(base64PubRsa, src, base64SignValue, ALGORITHM_MD5RSA,charset);
	}
	
	/**
	 * 公钥加密
	 */
	public static String encryptByRsaPub(String src, String base64PubRsa,String charset) {
		try {
			PublicKey publicKey = genPublicKey(base64PubRsa);
			byte[] encryptBytes = encryptByKey(src.getBytes(charset), publicKey);
			return Base64.encodeBase64String(encryptBytes);
		} catch (Exception e) {
			e.printStackTrace();
			throw new IllegalArgumentException("rsa加密失败");
		}
	}
	
	/**
	 * 私钥解密
	 */
	public static String decryptByRsaPri(String base64Src, String base64PriRsa,String charset) {
		try {
			PrivateKey privateKey = genPrivateKey(base64PriRsa);
			return new String(decryptKey(Base64.decodeBase64(base64Src), privateKey), charset);
		} catch (Exception e) {
			e.printStackTrace();
			throw new IllegalArgumentException("rsa解密失败");
		}
	}

	private static String sign2Base64ByAlg(String base64PriRsa, String inputStr, String algorithm,String charset) {
		try {
			PrivateKey privateKey = genPrivateKey(base64PriRsa);
			Signature signature = Signature.getInstance(algorithm, "BC");
			signature.initSign(privateKey);
			signature.update(inputStr.getBytes(charset));
			return Base64.encodeBase64String(signature.sign());
		} catch (Exception e) {
			e.printStackTrace();
			throw new IllegalArgumentException("rsa签名异常");
		}
	}
	
	private static boolean verifySignByAlg(String base64PubRsa, String src, String base64SignValue, String algorithm,String charset) {
		try {
			PublicKey publicKey = genPublicKey(base64PubRsa);
			Signature signature = Signature.getInstance(algorithm, "BC");
			signature.initVerify(publicKey);
			signature.update(src.getBytes(charset));
			return signature.verify(Base64.decodeBase64(base64SignValue));
		} catch (Exception e) {
			e.printStackTrace();
			throw new IllegalArgumentException("rsa验证签名异常");
		}
	}

	private static PrivateKey genPrivateKey(String base64Rsa) {
		try {
			KeyFactory kf = KeyFactory.getInstance(KEY_ALGORITHM, "BC");
			PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(Base64.decodeBase64(base64Rsa));
			return kf.generatePrivate(keySpec);
		} catch (Exception e) {
			e.printStackTrace();
			throw new IllegalArgumentException("初始化秘钥异常");
		}
	}

	private static PublicKey genPublicKey(String base64Rsa) {
		try {
			KeyFactory kf = KeyFactory.getInstance(KEY_ALGORITHM, "BC");
			X509EncodedKeySpec keySpec = new X509EncodedKeySpec(Base64.decodeBase64(base64Rsa));
			return kf.generatePublic(keySpec);
		} catch (Exception e) {
			e.printStackTrace();
			throw new IllegalArgumentException("初始化秘钥失败");
		}
	}
	
	public static String convertPubRsaFile2Str(String fileStr){
		PublicKey pk = genPublicKey(fileStr);
		return Base64.encodeBase64String(pk.getEncoded());
	}
	
	public static String convertPriRsaFile2Str(String fileStr){
		PrivateKey pk = genPrivateKey(fileStr);
		return Base64.encodeBase64String(pk.getEncoded());
	}
	/**
	 * 公钥加密
	 */
	private static byte[] encryptByKey(byte[] srcData, Key publicKey) {
		try {
			Cipher cipher = Cipher.getInstance("RSA/ECB/PKCS1Padding");
			cipher.init(Cipher.ENCRYPT_MODE, publicKey);
			// 分段加密
			int blockSize = cipher.getOutputSize(srcData.length) - 11;
			byte[] encryptedData = null;
			for (int i = 0; i < srcData.length; i += blockSize) {
				// 注意要使用2的倍数，否则会出现加密后的内容再解密时为乱码
				byte[] doFinal = cipher.doFinal(subarray(srcData, i, i + blockSize));
				encryptedData = addAll(encryptedData, doFinal);
			}
			return encryptedData;

		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	private static byte[] decryptKey(byte[] srcData, Key key) {
		try {
			Cipher cipher = Cipher.getInstance("RSA/ECB/PKCS1Padding");
			cipher.init(Cipher.DECRYPT_MODE, key);
			// 分段加密
			int blockSize = cipher.getOutputSize(srcData.length);
			byte[] decryptData = null;

			for (int i = 0; i < srcData.length; i += blockSize) {
				byte[] doFinal = cipher.doFinal(subarray(srcData, i, i + blockSize));

				decryptData = addAll(decryptData, doFinal);
			}
			return decryptData;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	private static byte[] subarray(byte[] array, int startIndexInclusive, int endIndexExclusive) {
		if (array == null) {
			return null;
		}
		if (startIndexInclusive < 0) {
			startIndexInclusive = 0;
		}
		if (endIndexExclusive > array.length) {
			endIndexExclusive = array.length;
		}
		int newSize = endIndexExclusive - startIndexInclusive;

		if (newSize <= 0) {
			return new byte[0];
		}

		byte[] subarray = new byte[newSize];

		System.arraycopy(array, startIndexInclusive, subarray, 0, newSize);

		return subarray;
	}

	private static byte[] addAll(byte[] array1, byte[] array2) {
		if (array1 == null) {
			return clone(array2);
		} else if (array2 == null) {
			return clone(array1);
		}
		byte[] joinedArray = new byte[array1.length + array2.length];
		System.arraycopy(array1, 0, joinedArray, 0, array1.length);
		System.arraycopy(array2, 0, joinedArray, array1.length, array2.length);
		return joinedArray;
	}

	public static byte[] clone(byte[] array) {
		if (array == null) {
			return null;
		}
		return (byte[]) array.clone();
	}
}
