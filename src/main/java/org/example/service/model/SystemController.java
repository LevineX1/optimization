package org.example.service.model;


import org.example.service.bigdata.KafkaDataProducer;
import org.example.service.bigdata.StreamingAnalysisEngine;
import org.example.service.tools.VisualizationGenerator;

import javax.imageio.ImageIO;

import java.awt.image.BufferedImage;
import java.io.File;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

/**
 * 系统主控制器
 */
public class SystemController {
  private final KafkaDataProducer producer;
  private final StreamingAnalysisEngine engine;
  private final PerformanceMetrics metrics;
  private final Map<String, BaseStation> stationMap;
  private final VisualizationGenerator visualizer;
  private Timer timer;

  public SystemController() {
    this.metrics = new PerformanceMetrics();
    this.stationMap = initializeBaseStations();
    this.producer = new KafkaDataProducer(metrics);
    this.engine = new StreamingAnalysisEngine(metrics, stationMap);
    this.visualizer = new VisualizationGenerator(new ArrayList<>(), metrics);
  }

  private Map<String, BaseStation> initializeBaseStations() {
    Map<String, BaseStation> stations = new HashMap<>();
    // 初始化测试基站数据
    stations.put("BS001", new BaseStation("BS001", "北京朝阳站", 116.4830, 39.9917,
        "华为", 3, "正常", LocalDateTime.now().minusDays(30)));
    stations.put("BS002", new BaseStation("BS002", "上海浦东站", 121.5373, 31.2152,
        "中兴", 2, "正常", LocalDateTime.now().minusDays(45)));
    stations.put("BS003", new BaseStation("BS003", "广州天河站", 113.3415, 23.1272,
        "爱立信", 4, "维护中", LocalDateTime.now().minusDays(15)));
    stations.put("BS004", new BaseStation("BS004", "深圳南山站", 113.9298, 22.5420,
        "诺基亚", 3, "正常", LocalDateTime.now().minusDays(60)));
    stations.put("BS005", new BaseStation("BS005", "成都高新站", 104.0665, 30.5728,
        "华为", 2, "正常", LocalDateTime.now().minusDays(20)));
    return stations;
  }

  public void startSystem() {
    System.out.println("启动无线设备障碍分析系统...");

    // 启动数据生产
    producer.startProducing();

    // 启动定时处理任务
    timer = new Timer();
    timer.scheduleAtFixedRate(new TimerTask() {
      @Override
      public void run() {
        processEvents();
      }
    }, 0, 1000); // 每秒处理一次

    System.out.println("系统启动完成");
  }

  public void stopSystem() {
    System.out.println("停止系统...");
    if (timer != null) {
      timer.cancel();
    }
    producer.stopProducing();
    System.out.println("系统已停止");
  }

  private void processEvents() {
    List<FaultEvent> events = producer.getEvents();
    for (FaultEvent event : events) {
      DispatchResult result = engine.analyzeFaultEvent(event);
      if (result.isSuccess()) {
        System.out.println("处理成功: " + event.getDeviceId() + " - " + event.getFaultType());
      } else {
        System.out.println("处理失败: " + result.getMessage());
      }
    }
  }

  public void generateReport() {
    try {
      // 生成可视化报告
      BufferedImage dashboard = visualizer.generateDashboardImage();
      File outputFile = new File("fault_analysis_dashboard.png");
      ImageIO.write(dashboard, "png", outputFile);
      System.out.println("报告已生成: " + outputFile.getAbsolutePath());

      // 输出统计信息
      System.out.println("\n=== 系统性能统计 ===");
      System.out.println(metrics.toString());
      System.out.println("\n=== 基站状态 ===");
      stationMap.values().forEach(System.out::println);

    } catch (Exception e) {
      System.err.println("生成报告失败: " + e.getMessage());
    }
  }

  public PerformanceMetrics getMetrics() {
    return metrics;
  }

  public Map<String, BaseStation> getStationMap() {
    return stationMap;
  }
}