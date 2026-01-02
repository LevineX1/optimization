package org.example.service.bigdata;


import org.example.service.model.BaseStation;
import org.example.service.model.DispatchResult;
import org.example.service.model.FaultEvent;
import org.example.service.model.PerformanceMetrics;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

/**
 * Spark Streaming处理引擎 - 实时故障分析
 */
public class StreamingAnalysisEngine {
  private static final int WINDOW_SIZE = 1000; // 滑动窗口大小
  private final PerformanceMetrics metrics;
  private final Map<String, BaseStation> stationMap;
  private final Map<String, List<FaultEvent>> recentEvents;

  public StreamingAnalysisEngine(PerformanceMetrics metrics, Map<String, BaseStation> stationMap) {
    this.metrics = metrics;
    this.stationMap = stationMap;
    this.recentEvents = new ConcurrentHashMap<>();
  }

  /**
   * 实时分析故障事件
   */
  public DispatchResult analyzeFaultEvent(FaultEvent event) {
    long startTime = System.currentTimeMillis();
    String taskId = UUID.randomUUID().toString();

    try {
      // 1. 数据验证
      if (!validateEvent(event)) {
        metrics.recordRequestFailure(System.currentTimeMillis() - startTime);
        return new DispatchResult(false, "数据验证失败", taskId, LocalDateTime.now());
      }

      // 2. 关联基站信息
      BaseStation station = stationMap.get(event.getBaseStationId());
      if (station == null) {
        metrics.recordRequestFailure(System.currentTimeMillis() - startTime);
        return new DispatchResult(false, "基站信息不存在", taskId, LocalDateTime.now());
      }

      // 3. 故障模式识别
      String detectedFaultType = detectFaultPattern(event, station);
      event.setFaultType(detectedFaultType);

      // 4. 严重程度评估
      int severity = assessSeverity(event, station);
      event.setSeverityLevel(severity);

      // 5. 更新滑动窗口
      updateSlidingWindow(event);

      // 6. 趋势分析
      analyzeTrend(event.getBaseStationId());

      metrics.recordRequestSuccess(System.currentTimeMillis() - startTime, detectedFaultType);
      return new DispatchResult(true, "分析完成", taskId, LocalDateTime.now());

    } catch (Exception e) {
      metrics.recordRequestFailure(System.currentTimeMillis() - startTime);
      return new DispatchResult(false, "分析异常: " + e.getMessage(), taskId, LocalDateTime.now());
    }
  }

  private boolean validateEvent(FaultEvent event) {
    return event != null &&
        event.getDeviceId() != null &&
        event.getSignalStrength() >= -150 && event.getSignalStrength() <= 0 &&
        event.getErrorRate() >= 0 && event.getErrorRate() <= 1 &&
        event.getThroughput() >= 0;
  }

  private String detectFaultPattern(FaultEvent event, BaseStation station) {
    // 基于规则的故障模式识别
    if (event.getThroughput() < 10) {
      return "零流量小区";
    } else if (event.getRsrp() < -110 && event.getSinr() < 5) {
      return "天线隐性故障";
    } else if (event.getErrorRate() > 0.05) {
      return "传输链路故障";
    } else if (event.getLatency() > 100) {
      return "突发拥塞";
    } else {
      return "设备硬件故障";
    }
  }

  private int assessSeverity(FaultEvent event, BaseStation station) {
    int score = 0;

    // 信号质量评分
    if (event.getRsrp() < -110) {
      score += 3;
    } else if (event.getRsrp() < -100) {
      score += 2;
    } else if (event.getRsrp() < -90) {
      score += 1;
    }

    // 误码率评分
    if (event.getErrorRate() > 0.08) {
      score += 3;
    } else if (event.getErrorRate() > 0.05) {
      score += 2;
    } else if (event.getErrorRate() > 0.02) {
      score += 1;
    }

    // 吞吐量评分
    if (event.getThroughput() < 10) {
      score += 3;
    } else if (event.getThroughput() < 50) {
      score += 2;
    } else if (event.getThroughput() < 100) {
      score += 1;
    }

    return Math.min(score, 5); // 限制在1-5级
  }

  private void updateSlidingWindow(FaultEvent event) {
    String key = event.getBaseStationId() + "_" + event.getCellId();
    recentEvents.computeIfAbsent(key, k -> Collections.synchronizedList(new ArrayList<>()))
        .add(event);

    // 保持窗口大小
    List<FaultEvent> events = recentEvents.get(key);
    if (events.size() > WINDOW_SIZE) {
      events.subList(0, events.size() - WINDOW_SIZE).clear();
    }
  }

  private void analyzeTrend(String baseStationId) {
    // 实现趋势分析逻辑
    List<FaultEvent> stationEvents = recentEvents.entrySet().stream()
        .filter(entry -> entry.getKey().startsWith(baseStationId + "_"))
        .flatMap(entry -> entry.getValue().stream())
        .collect(Collectors.toList());

    if (stationEvents.size() > 10) {
      // 计算最近10个事件的平均指标
      double avgErrorRate = stationEvents.stream()
          .skip(stationEvents.size() - 10)
          .mapToDouble(FaultEvent::getErrorRate)
          .average()
          .orElse(0.0);

      // 趋势预警逻辑
      if (avgErrorRate > 0.06) {
        System.out.println("预警: 基站 " + baseStationId + " 误码率趋势上升");
      }
    }
  }

  public Map<String, List<FaultEvent>> getRecentEvents() {
    return new HashMap<>(recentEvents);
  }
}