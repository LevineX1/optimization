package org.example.service.model;

import lombok.Getter;

import java.time.LocalDateTime;

/**
 * VoLTE指标具体实现
 */
public class VoLTEMetric extends NetworkMetric {
  @Getter
  private final double jitter;
  @Getter
  private final double packetLoss;

  public VoLTEMetric(String callId, double mosValue, double jitter, double packetLoss,
                     LocalDateTime timestamp) {
    super(callId, mosValue, timestamp, "VoLTE_MOS");
    this.jitter = jitter;
    this.packetLoss = packetLoss;
  }

  public double getMosValue() {
    return getValue();
  }
}
