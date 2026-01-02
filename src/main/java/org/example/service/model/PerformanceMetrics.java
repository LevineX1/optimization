package org.example.service.model;

import lombok.Getter;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

/**
 * 性能指标统计类
 */
@Getter
/**
 * 性能指标统计
 */
public class PerformanceMetrics {
  private final AtomicLong totalRequests;
  private final AtomicLong successfulRequests;
  private final AtomicLong failedRequests;
  private final AtomicLong totalProcessingTime;
  private final AtomicInteger currentConcurrentRequests;
  private final Map<String, AtomicLong> faultTypeStatistics;

  public PerformanceMetrics() {
    this.totalRequests = new AtomicLong(0);
    this.successfulRequests = new AtomicLong(0);
    this.failedRequests = new AtomicLong(0);
    this.totalProcessingTime = new AtomicLong(0);
    this.currentConcurrentRequests = new AtomicInteger(0);
    this.faultTypeStatistics = new ConcurrentHashMap<>();
  }

  // 记录请求开始
  public void recordRequestStart() {
    totalRequests.incrementAndGet();
    currentConcurrentRequests.incrementAndGet();
  }

  public void recordRequestEnd(long processingTime, boolean success, String faultType) {
    if (success)  {
      recordRequestSuccess(processingTime, faultType);
    } else {
      recordRequestFailure(processingTime);
    }
  }

  // 记录请求成功
  public void recordRequestSuccess(long processingTime, String faultType) {
    successfulRequests.incrementAndGet();
    totalProcessingTime.addAndGet(processingTime);
    currentConcurrentRequests.decrementAndGet();

    // 统计故障类型
    if (faultType != null) {
      faultTypeStatistics.computeIfAbsent(faultType, k -> new AtomicLong(0)).incrementAndGet();
    }
  }

  // 记录请求失败
  public void recordRequestFailure(long processingTime) {
    failedRequests.incrementAndGet();
    totalProcessingTime.addAndGet(processingTime);
    currentConcurrentRequests.decrementAndGet();
  }

  // 计算平均处理时间（毫秒）
  public double getAverageProcessingTime() {
    long total = totalRequests.get();
    if (total == 0) {
      return 0.0;
    }
    return (double) totalProcessingTime.get() / total;
  }

  // 计算成功率
  public double getSuccessRate() {
    long total = totalRequests.get();
    if (total == 0) {
      return 0.0;
    }
    return (double) successfulRequests.get() / total * 100;
  }

  // 获取故障类型分布
  public Map<String, Double> getFaultTypeDistribution() {
    Map<String, Double> distribution = new HashMap<>();
    long total = successfulRequests.get();
    if (total > 0) {
      for (Map.Entry<String, AtomicLong> entry : faultTypeStatistics.entrySet()) {
        distribution.put(entry.getKey(), (double) entry.getValue().get() / total * 100);
      }
    }
    return distribution;
  }

  public MetricStats calculateStats() {
    double averageProcessingTime = getAverageProcessingTime();
    double successRate = getSuccessRate();
    return new MetricStats(totalRequests.get(), failedRequests.get(), averageProcessingTime,
        successRate);
  }

  // 重置统计信息
  public void reset() {
    totalRequests.set(0);
    successfulRequests.set(0);
    failedRequests.set(0);
    totalProcessingTime.set(0);
    currentConcurrentRequests.set(0);
    faultTypeStatistics.clear();
  }

  @Override
  public String toString() {
    return String.format(
        "PerformanceMetrics{totalRequests=%d, successfulRequests=%d, failedRequests=%d, " +
            "successRate=%.2f%%, avgProcessingTime=%.2fms, currentConcurrent=%d, faultTypes=%s}",
        totalRequests.get(), successfulRequests.get(), failedRequests.get(),
        getSuccessRate(), getAverageProcessingTime(), currentConcurrentRequests.get(),
        getFaultTypeDistribution()
    );
  }
}
