package org.example.service.model;

import lombok.Getter;

import java.time.LocalDateTime;

/**
 * 网络指标基类
 */
@Getter
public abstract class NetworkMetric {
  private final String deviceId;
  private final double value;
  private final LocalDateTime timestamp;
  private final String metricType;

  public NetworkMetric(String deviceId, double value, LocalDateTime timestamp, String metricType) {
    this.deviceId = deviceId;
    this.value = value;
    this.timestamp = timestamp;
    this.metricType = metricType;
  }

}

