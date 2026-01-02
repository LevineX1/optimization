package org.example.service.model;

import lombok.Data;

import java.time.LocalDateTime;
import java.util.Objects;

/**
 * 故障事件类
 */
@Data
public class FaultEvent {
  private String deviceId;
  private double signalStrength;
  private double errorRate;
  private double throughput;
  private int severityLevel;
  private LocalDateTime occurTime;
  private String baseStationId;
  private String cellId;
  private double rsrp;      // 参考信号接收功率
  private double sinr;      // 信号干扰噪声比
  private double latency;   // 时延
  private String faultType; // 故障类型

  public FaultEvent(String deviceId, double signalStrength, double errorRate,
                    double throughput, int severityLevel, LocalDateTime occurTime,
                    String baseStationId, String cellId, double rsrp, double sinr,
                    double latency, String faultType) {
    this.deviceId = deviceId;
    this.signalStrength = signalStrength;
    this.errorRate = errorRate;
    this.throughput = throughput;
    this.severityLevel = severityLevel;
    this.occurTime = occurTime;
    this.baseStationId = baseStationId;
    this.cellId = cellId;
    this.rsrp = rsrp;
    this.sinr = sinr;
    this.latency = latency;
    this.faultType = faultType;
  }

  @Override
  public String toString() {
    return String.format("FaultEvent{deviceId='%s', signalStrength=%.2f, errorRate=%.4f, " +
            "throughput=%.2f, severityLevel=%d, occurTime=%s, " +
            "baseStationId='%s', cellId='%s', rsrp=%.1f, sinr=%.1f, latency=%.1f, faultType='%s'}",
        deviceId, signalStrength, errorRate, throughput, severityLevel, occurTime,
        baseStationId, cellId, rsrp, sinr, latency, faultType);
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    FaultEvent that = (FaultEvent) o;
    return Double.compare(that.signalStrength, signalStrength) == 0 &&
        Double.compare(that.errorRate, errorRate) == 0 &&
        Double.compare(that.throughput, throughput) == 0 &&
        severityLevel == that.severityLevel &&
        Double.compare(that.rsrp, rsrp) == 0 &&
        Double.compare(that.sinr, sinr) == 0 &&
        Double.compare(that.latency, latency) == 0 &&
        Objects.equals(deviceId, that.deviceId) &&
        Objects.equals(occurTime, that.occurTime) &&
        Objects.equals(baseStationId, that.baseStationId) &&
        Objects.equals(cellId, that.cellId) &&
        Objects.equals(faultType, that.faultType);
  }

  @Override
  public int hashCode() {
    return Objects.hash(deviceId, signalStrength, errorRate, throughput,
        severityLevel, occurTime, baseStationId, cellId, rsrp, sinr, latency, faultType);
  }
}