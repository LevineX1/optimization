package org.example.service.model;

import lombok.Getter;

/**
 * 根因预测结果
 */
@Getter
public class RootCausePrediction {
  private final String rootCause;
  private final double confidence;
  private final long timestamp;

  public RootCausePrediction(String rootCause, double confidence, long timestamp) {
    this.rootCause = rootCause;
    this.confidence = confidence;
    this.timestamp = timestamp;
  }

  // Getter方法...
}