package org.example.service.model;

import lombok.Getter;

import java.time.LocalDateTime;

/**
 * 网络节点类
 */
@Getter
public class NetworkNode {
  private final String nodeId;
  private final String nodeType;
  private final String location;
  private final double latitude;
  private final double longitude;
  private final LocalDateTime createTime;

  public NetworkNode(String nodeId, String nodeType, String location,
                     double latitude, double longitude) {
    this.nodeId = nodeId;
    this.nodeType = nodeType;
    this.location = location;
    this.latitude = latitude;
    this.longitude = longitude;
    this.createTime = LocalDateTime.now();
  }
}