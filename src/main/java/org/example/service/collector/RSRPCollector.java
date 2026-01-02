package org.example.service.collector;


import org.example.service.model.RSRPMetric;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 射频指标采集器 - RSRP指标采集
 */
public class RSRPCollector {
  private static final double MIN_RSRP = -110.0;
  private static final double MAX_RSRP = -65.0;
  private final Map<String, List<Double>> historicalData = new ConcurrentHashMap<>();

  public RSRPMetric collectRSRP(String deviceId, double rsrpValue) {
    if (rsrpValue < MIN_RSRP || rsrpValue > MAX_RSRP) {
      throw new IllegalArgumentException("RSRP值超出有效范围: " + rsrpValue);
    }

    RSRPMetric metric = new RSRPMetric(deviceId, rsrpValue, LocalDateTime.now());
    storeHistoricalData(deviceId, rsrpValue);
    return metric;
  }

  private void storeHistoricalData(String deviceId, double rsrpValue) {
    historicalData.computeIfAbsent(deviceId, k -> new ArrayList<>())
        .add(rsrpValue);
    // 保持最近1000个数据点
    if (historicalData.get(deviceId).size() > 1000) {
      historicalData.get(deviceId).remove(0);
    }
  }

  public double calculateAverageRSRP(String deviceId) {
    List<Double> data = historicalData.get(deviceId);
    if (data == null || data.isEmpty()) {
      return 0.0;
    }
    return data.stream().mapToDouble(Double::doubleValue).average().orElse(0.0);
  }
}