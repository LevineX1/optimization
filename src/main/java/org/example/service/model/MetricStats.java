package org.example.service.model;

import lombok.Getter;

/**
 * 指标统计结果
 */
@Getter
public class MetricStats {
  private final long totalRequests;
  private final long failedRequests;
  private final double averageProcessingTime;
  private final double successRate;

  public MetricStats(long totalRequests, long failedRequests, double averageProcessingTime,
                     double successRate) {
    this.totalRequests = totalRequests;
    this.failedRequests = failedRequests;
    this.averageProcessingTime = averageProcessingTime;
    this.successRate = successRate;
  }
}