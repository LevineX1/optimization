package org.example.service.model;

import lombok.Data;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * 故障信息
 */
@Data
public class FaultInfo {
  private final String faultId;
  private final String deviceId;
  private final String faultType;
  private final LocalDateTime occurrenceTime;
  private final Map<String, Object> faultDetails;

  public FaultInfo(String deviceId, String faultType, LocalDateTime occurrenceTime) {
    this.faultId = UUID.randomUUID().toString();
    this.deviceId = deviceId;
    this.faultType = faultType;
    this.occurrenceTime = occurrenceTime;
    this.faultDetails = new HashMap<>();
  }
}