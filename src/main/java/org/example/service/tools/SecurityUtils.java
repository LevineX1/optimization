package org.example.service.tools;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.util.Base64;

/**
 * 数据加密工具类
 */
public class SecurityUtils {
  private static final String AES_KEY = "TelecomXuzhou2025";
  private static final String CHARSET = "UTF-8";

  /**
   * AES加密
   */
  public static String encryptAES(String data) {
    try {
      SecretKeySpec secretKey = new SecretKeySpec(AES_KEY.getBytes(CHARSET), "AES");
      Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
      cipher.init(Cipher.ENCRYPT_MODE, secretKey);
      byte[] encrypted = cipher.doFinal(data.getBytes(CHARSET));
      return Base64.getEncoder().encodeToString(encrypted);
    } catch (Exception e) {
      throw new RuntimeException("加密失败", e);
    }
  }

  /**
   * AES解密
   */
  public static String decryptAES(String encryptedData) {
    try {
      SecretKeySpec secretKey = new SecretKeySpec(AES_KEY.getBytes(CHARSET), "AES");
      Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
      cipher.init(Cipher.DECRYPT_MODE, secretKey);
      byte[] decoded = Base64.getDecoder().decode(encryptedData);
      byte[] decrypted = cipher.doFinal(decoded);
      return new String(decrypted, CHARSET);
    } catch (Exception e) {
      throw new RuntimeException("解密失败", e);
    }
  }

  /**
   * SHA-256哈希
   */
  public static String hashSHA256(String data) {
    try {
      MessageDigest digest = MessageDigest.getInstance("SHA-256");
      byte[] hash = digest.digest(data.getBytes(StandardCharsets.UTF_8));
      StringBuilder hexString = new StringBuilder();
      for (byte b : hash) {
        String hex = Integer.toHexString(0xff & b);
        if (hex.length() == 1) {
          hexString.append('0');
        }
        hexString.append(hex);
      }
      return hexString.toString();
    } catch (Exception e) {
      throw new RuntimeException("哈希计算失败", e);
    }
  }
}