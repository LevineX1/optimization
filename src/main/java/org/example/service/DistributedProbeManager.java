/**
 * 基于新一代网络优化平台的无线设备障碍主动分析定位系统
 * 核心数据处理引擎 - Java实现版本
 */
package org.example.service;


import com.sun.corba.se.spi.orb.DataCollector;
import org.example.service.model.Alert;
import org.example.service.model.AlertManager;
import org.example.service.model.AlertSeverity;
import org.example.service.model.ProbeStatus;
import org.example.service.model.SystemController;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

/**
 * 无线设备数据采集探针管理器
 * 负责分布式探针的数据采集和状态监控
 */
public class DistributedProbeManager {
  private final Map<String, ProbeStatus> probeStatusMap = new ConcurrentHashMap<>();
  private final ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(10);
  private final DataCollector dataCollector;
  private final AlertManager alertManager;

  public DistributedProbeManager(DataCollector collector, AlertManager alertManager) {
    this.dataCollector = collector;
    this.alertManager = alertManager;
    initializeProbeMonitoring();
  }

  public static void main(String[] args) {
    SystemController controller = new SystemController();

    try {
      // 启动系统
      controller.startSystem();

      // 运行10秒后生成报告
      Thread.sleep(10000);

      // 生成测试报告
      controller.generateReport();

      // 停止系统
      controller.stopSystem();

    } catch (InterruptedException e) {
      Thread.currentThread().interrupt();
      controller.stopSystem();
    }
  }

  private void initializeProbeMonitoring() {
    // 每30秒检查一次探针状态
    scheduler.scheduleAtFixedRate(this::checkProbeHealth, 0, 30, TimeUnit.SECONDS);
  }

  /**
   * 探针健康检查
   */
  private void checkProbeHealth() {
    List<String> offlineProbes = probeStatusMap.entrySet().stream()
        .filter(entry -> !entry.getValue().isOnline())
        .map(Map.Entry::getKey)
        .collect(Collectors.toList());

    if (!offlineProbes.isEmpty()) {
      Alert alert = new Alert("PROBE_HEALTH", offlineProbes.size(),
          offlineProbes.size(), LocalDateTime.now(), AlertSeverity.CRITICAL);
      alertManager.submitAlert(alert);
    }
  }
}