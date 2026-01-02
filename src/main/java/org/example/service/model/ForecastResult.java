package org.example.service.model;

import lombok.Getter;

@Getter
public class ForecastResult {
  private final double predictedValue;
  private final double dynamicThreshold;
  private final double errorRate;

  public ForecastResult(double predictedValue, double dynamicThreshold, double errorRate) {
    this.predictedValue = predictedValue;
    this.dynamicThreshold = dynamicThreshold;
    this.errorRate = errorRate;
  }

  // Getter方法...
}