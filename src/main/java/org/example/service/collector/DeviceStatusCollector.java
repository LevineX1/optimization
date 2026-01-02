package org.example.service.collector;


import org.example.service.model.DeviceTiltMetric;

import java.time.LocalDateTime;

/**
 * 设备状态采集器 - RRU倾角采集
 */
public class DeviceStatusCollector {
  private static final double ANGLE_PRECISION = 0.1;

  public DeviceTiltMetric collectTiltAngle(String deviceId, double horizontalAngle,
                                           double verticalAngle, double temperature) {
    validateAngles(horizontalAngle, verticalAngle);
    return new DeviceTiltMetric(deviceId, horizontalAngle, verticalAngle,
        temperature, LocalDateTime.now());
  }

  private void validateAngles(double horizontal, double vertical) {
    if (Math.abs(horizontal) > 360 || Math.abs(vertical) > 180) {
      throw new IllegalArgumentException("角度值超出合理范围");
    }
  }

  public boolean isAngleAbnormal(DeviceTiltMetric current, DeviceTiltMetric previous) {
    double horizontalDiff = Math.abs(current.getHorizontalAngle() - previous.getHorizontalAngle());
    double verticalDiff = Math.abs(current.getVerticalAngle() - previous.getVerticalAngle());
    return horizontalDiff > ANGLE_PRECISION * 5 || verticalDiff > ANGLE_PRECISION * 5;
  }
}