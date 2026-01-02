package org.example.service.model;

import lombok.Getter;

/**
 * 模型性能统计
 */
@Getter
public class ModelPerformance {
  private final double trainingAccuracy;
  private final double testAccuracy;
  private final long inferenceSpeed; // ms

  public ModelPerformance(double trainingAccuracy, double testAccuracy, long inferenceSpeed) {
    this.trainingAccuracy = trainingAccuracy;
    this.testAccuracy = testAccuracy;
    this.inferenceSpeed = inferenceSpeed;
  }

  // Getter方法...
}