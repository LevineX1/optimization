package org.example.service.model;

import lombok.Getter;

import java.time.LocalDateTime;
import java.util.UUID;

/**
 * 告警信息
 */
@Getter
public class Alert {
  private final String alertId;
  private final String source;
  private final double currentValue;
  private final double expectedValue;
  private final LocalDateTime triggerTime;
  private final AlertSeverity severity;

  public Alert(String source, double currentValue, double expectedValue,
               LocalDateTime triggerTime, AlertSeverity severity) {
    this.alertId = UUID.randomUUID().toString();
    this.source = source;
    this.currentValue = currentValue;
    this.expectedValue = expectedValue;
    this.triggerTime = triggerTime;
    this.severity = severity;
  }

  // Getter方法...
}