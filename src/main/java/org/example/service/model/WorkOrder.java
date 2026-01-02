package org.example.service.model;

import lombok.Data;

import java.time.LocalDateTime;

/**
 * 工单信息
 */
@Data
public class WorkOrder {
  private final String orderId;
  private final String orderType;
  private final String description;
  private final int priority;
  private final int slaHours;
  private final LocalDateTime createTime;
  private WorkOrderStatus status = WorkOrderStatus.PENDING;

  public WorkOrder(String orderId, String orderType, String description,
                   int priority, int slaHours, LocalDateTime createTime) {
    this.orderId = orderId;
    this.orderType = orderType;
    this.description = description;
    this.priority = priority;
    this.slaHours = slaHours;
    this.createTime = createTime;
  }
}