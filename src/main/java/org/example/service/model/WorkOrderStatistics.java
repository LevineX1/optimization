package org.example.service.model;

/**
 * 工单统计类
 */
public class WorkOrderStatistics {
  private long totalDispatches = 0;
  private long successfulDispatches = 0;
  private long autoDispatches = 0;
  private long totalProcessingTime = 0;

  public synchronized void recordDispatch(boolean success) {
    totalDispatches++;
    if (success) successfulDispatches++;
    autoDispatches++; // 当前系统全部为自动派单
  }

  public void recordProcessingTime(long processingTime) {
    totalProcessingTime += processingTime;
  }

  public double calculateEfficiency() {
    if (totalDispatches == 0) return 0.0;
    double successRate = (double) successfulDispatches / totalDispatches;
    double autoRate = (double) autoDispatches / totalDispatches;
    return (successRate * 0.6 + autoRate * 0.4) * 100; // 加权计算效率
  }

  // Getter方法
  public long getTotalDispatches() {
    return totalDispatches;
  }

  public long getAutoDispatches() {
    return autoDispatches;
  }
}