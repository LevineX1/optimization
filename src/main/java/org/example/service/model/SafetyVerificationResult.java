package org.example.service.model;

import lombok.Getter;

import java.time.LocalDateTime;

/**
 * 安全性验证结果
 */
@Getter
public class SafetyVerificationResult {
  private final boolean safe;
  private final String details;
  private final LocalDateTime verificationTime;

  public SafetyVerificationResult(boolean safe, String details, LocalDateTime verificationTime) {
    this.safe = safe;
    this.details = details;
    this.verificationTime = verificationTime;
  }

  // Getter方法...
}