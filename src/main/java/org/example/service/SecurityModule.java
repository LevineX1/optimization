package org.example.service;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.util.Arrays;
import java.util.Base64;

/**
 * 数据安全加密模块
 */
public class SecurityModule {
  private static final String AES_KEY = "a3b20d0f810ad3be"; // 项目编号作为密钥基础
  private static final String TRANSFORMATION = "AES/ECB/PKCS5Padding";

  public String encryptData(String data) {
    try {
      SecretKeySpec keySpec = new SecretKeySpec(generateKey(), "AES");
      Cipher cipher = Cipher.getInstance(TRANSFORMATION);
      cipher.init(Cipher.ENCRYPT_MODE, keySpec);
      byte[] encrypted = cipher.doFinal(data.getBytes(StandardCharsets.UTF_8));
      return Base64.getEncoder().encodeToString(encrypted);
    } catch (Exception e) {
      throw new SecurityException("数据加密失败", e);
    }
  }

  public String decryptData(String encryptedData) {
    try {
      SecretKeySpec keySpec = new SecretKeySpec(generateKey(), "AES");
      Cipher cipher = Cipher.getInstance(TRANSFORMATION);
      cipher.init(Cipher.DECRYPT_MODE, keySpec);
      byte[] decoded = Base64.getDecoder().decode(encryptedData);
      byte[] decrypted = cipher.doFinal(decoded);
      return new String(decrypted, StandardCharsets.UTF_8);
    } catch (Exception e) {
      throw new SecurityException("数据解密失败", e);
    }
  }

  private byte[] generateKey() {
    try {
      MessageDigest digest = MessageDigest.getInstance("SHA-256");
      byte[] key = digest.digest(AES_KEY.getBytes(StandardCharsets.UTF_8));
      return Arrays.copyOf(key, 16); // AES-128
    } catch (Exception e) {
      throw new SecurityException("密钥生成失败", e);
    }
  }

  public String calculateDataHash(String data) {
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
      throw new SecurityException("哈希计算失败", e);
    }
  }
}