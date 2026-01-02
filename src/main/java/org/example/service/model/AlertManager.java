package org.example.service.model;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * 告警管理器
 */
public class AlertManager {
  private static final AlertManager INSTANCE = new AlertManager();
  private final List<Alert> activeAlerts = new CopyOnWriteArrayList<>();
  private final List<Alert> historicalAlerts = new CopyOnWriteArrayList<>();

  public static AlertManager getInstance() {
    return INSTANCE;
  }

  public void submitAlert(Alert alert) {
    activeAlerts.add(alert);
    historicalAlerts.add(alert);
    // 触发告警处理逻辑
    processAlert(alert);
  }

  private void processAlert(Alert alert) {
    // 告警处理逻辑
    System.out.println("处理告警: " + alert.getSource() + " - " + alert.getSeverity());
  }

  public List<Alert> getActiveAlerts() {
    return new ArrayList<>(activeAlerts);
  }

  public void resolveAlert(String alertId) {
    activeAlerts.removeIf(alert -> alert.getAlertId().equals(alertId));
  }
}
    