package org.example.service.model;

import lombok.Getter;

/**
 * 工单模板类
 */
@Getter
public class WorkOrderTemplate {
  private final String faultType;
  private final String orderPrefix;
  private final int priority; // 1-最高，5-最低
  private final int slaHours; // SLA时限（小时）

  public WorkOrderTemplate(String faultType, String orderPrefix, int priority, int slaHours) {
    this.faultType = faultType;
    this.orderPrefix = orderPrefix;
    this.priority = priority;
    this.slaHours = slaHours;
  }

  public String getOrderType() {
    return orderPrefix + "_WORKORDER";
  }
}