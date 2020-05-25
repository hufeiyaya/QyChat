package com.vocust.qywx.demo.utils;

import java.security.KeyFactory;
import java.security.interfaces.RSAPrivateKey;
import java.security.spec.PKCS8EncodedKeySpec;
import java.util.Base64;

import javax.crypto.Cipher;

/**
 * Java RSA 加密工具类 参考： https://blog.csdn.net/qy20115549/article/details/83105736
 */
public class RSAUtils {

	private static final String privKeyPEM = "-----BEGIN PRIVATE KEY-----\n"
			+ "MIIEvQIBADANBgkqhkiG9w0BAQEFAASCBKcwggSjAgEAAoIBAQCc2YtXiEDCOKCw\n"
			+ "qe/Yfx4s7UNeThtN6oxhqVd4uKLu1rqiCO7juFQDUtWX83rKZ1loz9QqovwLrnze\n"
			+ "QAVW4LfHHVz6uw6QEknSoB1Z2M6z5kGknXmhzLEeZxc98DNzbenF4RwytUZHSQ/w\n"
			+ "me+p/Hml8vNForOVrROxEiYDNOTxExS8GLneFC32i5nS9HNxX8Skxd6XEknQprj/\n"
			+ "QgIJtkHefSXwBRnyxdmXuPjAJ+EeiG1yBsAWjjNxhHKKqIp4n5Z4eXVlMlUOTlS8\n"
			+ "ZkEd+NbUzWBE/RYPxb/VCGJHZI1YG3A4aJyZvizpA+GPxDKX6WlZ4pvZk4BE13Ff\n"
			+ "WUkY7xLdAgMBAAECggEAbknBYuEZt/edddtoHLD1hN2oWy1MobLk1t799JLPbmVq\n"
			+ "gfkKEPFRkayzJ886bgvRoJNSOlC0LXqZrqURnA8S6l1JPbw99ywE4KdZPA3o+jJT\n"
			+ "we5A0nZdrGUMwK6fXvDQSiWJcFZqfkFvhdyX2sz+tKNqT3KiGALFL+L0+5g7yw5n\n"
			+ "xrnfHHBY0axTj4hIxErSqeMUYOBZMBon7iCSTPkEXdy8CQ1D9jEhedIsXwPQ/aTX\n"
			+ "19fggN2eksMrBMA64YWj/zS2OKFsXNSZDW8Iy4VVQGUtHikUwudSfM+OshoD1X+t\n"
			+ "yHuQ9opirHla5GTAX7paDVKHrAOCpQ30nHEIGVJ9eQKBgQDNoVIPmM2qmO8iDunF\n"
			+ "b6s1RIKeiEZZwddxdf43OePqrM5cwKT5mnooJvqkhAbaTeLrDfD0m/+As0i0aAzT\n"
			+ "hDYRx5jfK/GajMr3P4MQX1MVPok7NUj+iPHgGfwlsTqzqdVmPl/R0xTLVNFJL2rg\n"
			+ "/FB53O8rE6s7flSFmy4Ggcp2YwKBgQDDRUxh58V6eiA1+Cx0USu6NBxIpYT2cadK\n"
			+ "x2dduBT/Nxk/nUv8/nAdmNrTpRy/vkrtXxNndOxz6+ZFFdiolDalOrCJWZ69vNmE\n"
			+ "hPEzG7LDMDwi/5pjTR5aFIV9XQovdR+LbP0P9hi2yurVzvUKhPMtYEhDv/IH/0W0\n"
			+ "CH1/fFz1vwKBgBcr24xoYpybuS60UYFvEPLisnlcz9ijcDyX2Dqu6lDyghqPlseN\n"
			+ "scA+jDXnrsIAztY/7uwRfBwrl8DEjNV064bbYA2d7Q7GWMS9wm4g38LgFiS232/X\n"
			+ "TLI9G12/9+Zw99waoT+Ksqgq+Z5umnOZ2xOwYr8O61e755haoTgYfhETAoGBAJaX\n"
			+ "pPaEdgmkkZ7NjAsQZnatpXppHAerUucarR7bzmWcqn4i4u1zlCdfMLO0TF2bddFm\n"
			+ "V2KcC5bCwM1ascIgu5vljSv/z6poMCBBHnrZy504X8hl8zzNxrCVzvUFgctjujYe\n"
			+ "kXNzyU9/YmgNuXCpY5kE/fYx24hLWAgl3BMrZ82lAoGAavBM8P5Clefeh5WoEZ5X\n"
			+ "tS1NKeIWjRmk3WqgjN+I95bDi1ACPUQ2AI6KxBZiO6L/dQ2d1B3Ny/YWSBTzcnup\n"
			+ "psH8/TCUqUIBb0UvaTA8mjpMqERxe0hQXuvds1zJUKZS7BI0hW66cbe7yS9qq3W5\n" + "komTPrVN7tra95unoojaMeM=\n"
			+ "-----END PRIVATE KEY-----";

	// 用此方法先获取秘钥
	public static String getPrivateKey(String str) throws Exception {

		String privKeyPEMnew = privKeyPEM.replaceAll("\\n", "").replace("-----BEGIN PRIVATE KEY-----", "").replace("-----END PRIVATE KEY-----", "");

		byte[] decoded = Base64.getDecoder().decode(privKeyPEMnew);
		RSAPrivateKey priKey = (RSAPrivateKey) KeyFactory.getInstance("RSA")
				.generatePrivate(new PKCS8EncodedKeySpec(decoded));
		// 64位解码加密后的字符串
		byte[] inputByte = Base64.getDecoder().decode(str);

		// RSA解密
		Cipher cipher = Cipher.getInstance("RSA");
		cipher.init(Cipher.DECRYPT_MODE, priKey);
		String outStr = new String(cipher.doFinal(inputByte));
		return outStr;
	}
}