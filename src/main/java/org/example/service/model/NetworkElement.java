package org.example.service.model;

import lombok.Getter;

/**
 * 网络元素定义
 */
@Getter
public class NetworkElement {
  private final String id;
  private final String type;
  private final String location;
  private final double latitude;
  private final double longitude;

  public NetworkElement(String id, String type, String location, double latitude,
                        double longitude) {
    this.id = id;
    this.type = type;
    this.location = location;
    this.latitude = latitude;
    this.longitude = longitude;
  }

}