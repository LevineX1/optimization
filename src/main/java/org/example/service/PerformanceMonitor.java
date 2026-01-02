package org.example.service;



import org.example.service.model.MetricStats;
import org.example.service.model.PerformanceMetrics;
import org.example.service.model.PerformanceReport;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 性能监控和指标收集
 */
public class PerformanceMonitor {
  private final Map<String, PerformanceMetrics> metricsMap = new ConcurrentHashMap<>();
  private final long startTime = System.currentTimeMillis();

  public void recordMetric(String operation, long duration, boolean success) {
    PerformanceMetrics metrics = metricsMap.computeIfAbsent(operation,
        k -> new PerformanceMetrics());
    metrics.recordRequestEnd(duration, success, operation);
  }

  public PerformanceReport generateReport() {
    Map<String, MetricStats> stats = new HashMap<>();
    for (Map.Entry<String, PerformanceMetrics> entry : metricsMap.entrySet()) {
      stats.put(entry.getKey(), entry.getValue().calculateStats());
    }

    long uptime = System.currentTimeMillis() - startTime;
    double availability = calculateAvailability();

    return new PerformanceReport(stats, uptime, availability, LocalDateTime.now());
  }

  private double calculateAvailability() {
    long totalOperations = metricsMap.values().stream().mapToLong(
        (metric) -> {
          return metric.getTotalRequests().get();
        }
    ).sum();
    long failedOperations = metricsMap.values().stream().mapToLong(
        (metric) -> {
          return metric.getFailedRequests().get();
        }
    ).sum();

    return totalOperations > 0 ?
        (double) (totalOperations - failedOperations) / totalOperations : 1.0;
  }
}