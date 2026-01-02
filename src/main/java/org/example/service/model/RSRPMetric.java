package org.example.service.model;

import java.time.LocalDateTime;

/**
 * RSRP指标具体实现
 */
public class RSRPMetric extends NetworkMetric {
  public RSRPMetric(String deviceId, double rsrpValue, LocalDateTime timestamp) {
    super(deviceId, rsrpValue, timestamp, "RSRP");
  }
}
