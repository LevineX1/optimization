package org.example.service.bigdata;


import org.example.service.model.FaultEvent;
import org.example.service.model.PerformanceMetrics;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

/**
 * Kafka数据生产者 - 模拟数据采集
 */
public class KafkaDataProducer {
  private final List<FaultEvent> eventBuffer;
  private final PerformanceMetrics metrics;
  private volatile boolean running = false;

  public KafkaDataProducer(PerformanceMetrics metrics) {
    this.eventBuffer = Collections.synchronizedList(new ArrayList<>());
    this.metrics = metrics;
  }

  public void startProducing() {
    running = true;
    Thread producerThread = new Thread(() -> {
      Random random = new Random();
      String[] faultTypes = {"零流量小区", "天线隐性故障", "突发拥塞", "设备硬件故障", "传输链路故障"};
      String[] baseStations = {"BS001", "BS002", "BS003", "BS004", "BS005"};

      while (running) {
        try {
          // 模拟数据产生
          FaultEvent event = new FaultEvent(
              "DEVICE_" + random.nextInt(1000),
              -80 + random.nextDouble() * 40, // 信号强度
              random.nextDouble() * 0.1,       // 误码率
              50 + random.nextDouble() * 950,  // 吞吐量
              random.nextInt(5) + 1,           // 严重等级
              LocalDateTime.now(),
              baseStations[random.nextInt(baseStations.length)],
              "CELL_" + random.nextInt(100),
              -120 + random.nextDouble() * 40, // RSRP
              5 + random.nextDouble() * 25,    // SINR
              10 + random.nextDouble() * 90,   // 时延
              faultTypes[random.nextInt(faultTypes.length)]
          );

          eventBuffer.add(event);
          metrics.recordRequestStart();

          Thread.sleep(100 + random.nextInt(400)); // 模拟数据产生间隔
        } catch (InterruptedException e) {
          Thread.currentThread().interrupt();
          break;
        }
      }
    });
    producerThread.setDaemon(true);
    producerThread.start();
  }

  public void stopProducing() {
    running = false;
  }

  public List<FaultEvent> getEvents() {
    synchronized (eventBuffer) {
      List<FaultEvent> events = new ArrayList<>(eventBuffer);
      eventBuffer.clear();
      return events;
    }
  }
}