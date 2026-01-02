package org.example.service.bigdata;



import org.example.service.algorithm.HoltWintersAlgorithm;
import org.example.service.model.Alert;
import org.example.service.model.AlertManager;
import org.example.service.model.AlertSeverity;
import org.example.service.model.ForecastResult;
import org.example.service.model.NetworkMetric;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * 实时流处理引擎 - 基于Kafka和Spark Streaming的模拟实现
 */
public class StreamProcessingEngine {
  private final BlockingQueue<NetworkMetric> inputQueue = new LinkedBlockingQueue<>(10000);
  private final Map<String, List<NetworkMetric>> slidingWindows = new ConcurrentHashMap<>();
  private final HoltWintersAlgorithm holtWinters;
  private final int windowSize = 15; // 15秒窗口
  private final int slideInterval = 5; // 5秒滑动

  public StreamProcessingEngine() {
    this.holtWinters = new HoltWintersAlgorithm(0.2, 0.1, 0.3, 300); // 5分钟季节性
    startStreamProcessing();
  }

  public void submitMetric(NetworkMetric metric) {
    try {
      inputQueue.put(metric);
    } catch (InterruptedException e) {
      Thread.currentThread().interrupt();
      throw new RuntimeException("数据提交被中断", e);
    }
  }

  private void startStreamProcessing() {
    Thread processorThread = new Thread(() -> {
      while (!Thread.currentThread().isInterrupted()) {
        try {
          processMetrics();
          Thread.sleep(slideInterval * 1000L);
        } catch (InterruptedException e) {
          Thread.currentThread().interrupt();
          break;
        }
      }
    });
    processorThread.setDaemon(true);
    processorThread.start();
  }

  private void processMetrics() {
    List<NetworkMetric> currentBatch = new ArrayList<>();
    inputQueue.drainTo(currentBatch, 1000); // 每次最多处理1000条

    for (NetworkMetric metric : currentBatch) {
      String key = metric.getDeviceId() + "_" + metric.getMetricType();
      slidingWindows.computeIfAbsent(key, k -> new ArrayList<>())
          .add(metric);

      // 维护滑动窗口大小
      List<NetworkMetric> window = slidingWindows.get(key);
      if (window.size() > windowSize) {
        window.subList(0, window.size() - windowSize).clear();
      }

      // 应用Holt-Winters算法进行异常检测
      if (window.size() >= windowSize) {
        detectAnomalies(key, window);
      }
    }
  }

  private void detectAnomalies(String key, List<NetworkMetric> window) {
    double[] values = window.stream()
        .mapToDouble(NetworkMetric::getValue)
        .toArray();

    ForecastResult forecast = holtWinters.forecast(values);
    double currentValue = values[values.length - 1];

    if (Math.abs(currentValue - forecast.getPredictedValue()) > forecast.getDynamicThreshold()) {
      // 触发异常告警
      Alert alert = new Alert(key, currentValue, forecast.getPredictedValue(),
          LocalDateTime.now(), AlertSeverity.WARNING);
      AlertManager.getInstance().submitAlert(alert);
    }
  }

  public long getQueueSize() {
    return inputQueue.size();
  }

  public int getActiveWindows() {
    return slidingWindows.size();
  }
}