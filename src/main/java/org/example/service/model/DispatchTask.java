package org.example.service.model;

import lombok.Getter;

/**
 * 派单任务类
 */
@Getter
public class DispatchTask {
  private final WorkOrder workOrder;
  private final long createTimestamp;

  public DispatchTask(WorkOrder workOrder, long createTimestamp) {
    this.workOrder = workOrder;
    this.createTimestamp = createTimestamp;
  }
}