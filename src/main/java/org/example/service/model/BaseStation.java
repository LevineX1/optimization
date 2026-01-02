package org.example.service.model;

import lombok.Data;

import java.time.LocalDateTime;

/**
 * 基站设备信息
 */
@Data
public class BaseStation {
  private String stationId;
  private String stationName;
  private double longitude; // 经度
  private double latitude;  // 纬度
  private String manufacturer; // 设备厂家
  private int rruCount;     // RRU/AAU数量
  private String status;    // 状态
  private LocalDateTime lastMaintenance; // 最后维护时间

  public BaseStation(String stationId, String stationName, double longitude,
                     double latitude, String manufacturer, int rruCount,
                     String status, LocalDateTime lastMaintenance) {
    this.stationId = stationId;
    this.stationName = stationName;
    this.longitude = longitude;
    this.latitude = latitude;
    this.manufacturer = manufacturer;
    this.rruCount = rruCount;
    this.status = status;
    this.lastMaintenance = lastMaintenance;
  }


  @Override
  public String toString() {
    return String.format("BaseStation{stationId='%s', stationName='%s', " +
            "longitude=%.6f, latitude=%.6f, manufacturer='%s', " +
            "rruCount=%d, status='%s', lastMaintenance=%s}",
        stationId, stationName, longitude, latitude, manufacturer,
        rruCount, status, lastMaintenance);
  }
}