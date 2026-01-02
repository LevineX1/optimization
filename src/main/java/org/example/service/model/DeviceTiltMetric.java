package org.example.service.model;

import lombok.Getter;

import java.time.LocalDateTime;

/**
 * 设备倾角指标
 */
@Getter
public class DeviceTiltMetric extends NetworkMetric {
  private final double horizontalAngle;
  private final double verticalAngle;
  private final double temperature;

  public DeviceTiltMetric(String deviceId, double horizontalAngle,
                          double verticalAngle, double temperature, LocalDateTime timestamp) {
    super(deviceId, 0.0, timestamp, "DEVICE_TILT");
    this.horizontalAngle = horizontalAngle;
    this.verticalAngle = verticalAngle;
    this.temperature = temperature;
  }
}
