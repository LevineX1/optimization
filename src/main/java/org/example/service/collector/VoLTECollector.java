package org.example.service.collector;



import org.example.service.model.VoLTEMetric;

import java.time.LocalDateTime;

/**
 * VoLTE业务质量采集器
 */
public class VoLTECollector {
  private static final double MIN_MOS = 1.0;
  private static final double MAX_MOS = 5.0;

  public VoLTEMetric collectMOS(String callId, double mosValue, double jitter, double packetLoss) {
    validateMOSValue(mosValue);
    return new VoLTEMetric(callId, mosValue, jitter, packetLoss, LocalDateTime.now());
  }

  private void validateMOSValue(double mosValue) {
    if (mosValue < MIN_MOS || mosValue > MAX_MOS) {
      throw new IllegalArgumentException("MOS值超出有效范围: " + mosValue);
    }
  }

  public double calculateQualityScore(VoLTEMetric metric) {
    double baseScore = metric.getMosValue() * 20; // MOS占60%
    double jitterScore = Math.max(0, 100 - metric.getJitter() * 10); // 抖动影响
    double lossScore = Math.max(0, 100 - metric.getPacketLoss() * 1000); // 丢包影响
    return (baseScore + jitterScore + lossScore) / 3;
  }
}