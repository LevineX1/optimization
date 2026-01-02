package org.example.service.model;

import com.alibaba.fastjson.JSONObject;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.Map;

/**
 * 性能报告
 */
public class PerformanceReport {
  private final Map<String, MetricStats> operationStats;
  private final long uptimeMillis;
  private final double availability;
  private final LocalDateTime reportTime;

  public PerformanceReport(Map<String, MetricStats> operationStats, long uptimeMillis,
                           double availability, LocalDateTime reportTime) {
    this.operationStats = Collections.unmodifiableMap(operationStats);
    this.uptimeMillis = uptimeMillis;
    this.availability = availability;
    this.reportTime = reportTime;
  }

  public JSONObject toJson() {
    JSONObject json = new JSONObject();
    json.put("uptime", uptimeMillis);
    json.put("availability", availability);
    json.put("reportTime", reportTime.toString());

    JSONObject statsJson = new JSONObject();
    for (Map.Entry<String, MetricStats> entry : operationStats.entrySet()) {
      JSONObject metricJson = new JSONObject();
      metricJson.put("totalOperations", entry.getValue().getTotalRequests());
      metricJson.put("successRate", entry.getValue().getSuccessRate());
      metricJson.put("averageDuration", entry.getValue().getAverageProcessingTime());
      statsJson.put(entry.getKey(), metricJson);
    }
    json.put("operationStats", statsJson);

    return json;
  }
}