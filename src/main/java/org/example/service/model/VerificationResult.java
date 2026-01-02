package org.example.service.model;

import lombok.Data;

/**
 * 验证结果
 */
@Data
public class VerificationResult {
  private final boolean valid;
  private final double accuracy;
  private final String message;

  public VerificationResult(boolean valid, double accuracy, String message) {
    this.valid = valid;
    this.accuracy = accuracy;
    this.message = message;
  }

  // Getter方法...
}