package org.example.service;



import org.example.service.model.DispatchResult;
import org.example.service.model.DispatchTask;
import org.example.service.model.FaultInfo;
import org.example.service.model.PerformanceMetrics;
import org.example.service.model.RootCausePrediction;
import org.example.service.model.WorkOrder;
import org.example.service.model.WorkOrderStatistics;
import org.example.service.model.WorkOrderTemplate;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.TimeUnit;

/**
 * 自动派单系统
 */
public class AutoDispatchSystem {
  private final Map<String, WorkOrderTemplate> workOrderTemplates = new ConcurrentHashMap<>();
  private final BlockingQueue<DispatchTask> dispatchQueue = new LinkedBlockingQueue<>();
  private final ScheduledExecutorService dispatcherScheduler =
      Executors.newScheduledThreadPool(5);
  private final PerformanceMetrics performanceMetrics = new PerformanceMetrics();
  private final Map<String, WorkOrderStatistics> orderStatistics = new ConcurrentHashMap<>();

  public AutoDispatchSystem() {
    initializeTemplates();
    startDispatcher();
  }

  private void initializeTemplates() {
    // 初始化工单模板
    workOrderTemplates.put("硬件故障", new WorkOrderTemplate("硬件故障", "HW_REPAIR", 2, 24));
    workOrderTemplates.put("传输问题", new WorkOrderTemplate("传输问题", "TRANSMISSION_FIX", 1, 12));
    workOrderTemplates.put("配置错误", new WorkOrderTemplate("配置错误", "CONFIG_UPDATE", 1, 6));
    workOrderTemplates.put("未知原因", new WorkOrderTemplate("未知原因", "INVESTIGATION", 3, 48));
  }

  private void startDispatcher() {
    // 每10秒处理一次派单队列
    dispatcherScheduler.scheduleAtFixedRate(this::processDispatchQueue, 0, 10, TimeUnit.SECONDS);
  }

  /**
   * 派发工单
   */
  public DispatchResult dispatchWorkOrder(FaultInfo faultInfo, RootCausePrediction prediction) {
    long startTime = System.currentTimeMillis();
    boolean success = false;

    try {
      WorkOrderTemplate template = workOrderTemplates.get(prediction.getRootCause());
      if (template == null) {
        template = workOrderTemplates.get("未知原因");
      }

      WorkOrder workOrder = createWorkOrder(faultInfo, prediction, template);
      DispatchTask dispatchTask = new DispatchTask(workOrder, System.currentTimeMillis());

      // 加入派单队列
      boolean queued = dispatchQueue.offer(dispatchTask);
      if (queued) {
        updateStatistics(workOrder.getOrderType(), true);
        success = true;
        return new DispatchResult(true, workOrder.getOrderId(), "工单已加入派单队列", LocalDateTime.now());
      } else {
        updateStatistics(workOrder.getOrderType(), false);
        return new DispatchResult(false, null, "派单队列已满，请稍后重试", LocalDateTime.now());
      }

    } finally {
      long duration = System.currentTimeMillis() - startTime;
      performanceMetrics.recordRequestEnd(duration, success, null);
    }
  }

  private WorkOrder createWorkOrder(FaultInfo faultInfo, RootCausePrediction prediction,
                                    WorkOrderTemplate template) {
    String orderId = generateOrderId(template.getOrderPrefix());
    String description = String.format("设备%s发生%s故障，置信度%.2f%%",
        faultInfo.getDeviceId(), prediction.getRootCause(), prediction.getConfidence() * 100);

    return new WorkOrder(orderId, template.getOrderType(), description,
        template.getPriority(), template.getSlaHours(), LocalDateTime.now());
  }

  private String generateOrderId(String prefix) {
    return prefix + "_" + System.currentTimeMillis() + "_" + ThreadLocalRandom.current().nextInt(1000, 9999);
  }

  private void processDispatchQueue() {
    List<DispatchTask> tasks = new ArrayList<>();
    dispatchQueue.drainTo(tasks, 50); // 每次最多处理50个任务

    for (DispatchTask task : tasks) {
      try {
        boolean dispatched = simulateDispatch(task.getWorkOrder());
        if (dispatched) {
          System.out.printf("工单%s派发成功: %s%n",
              task.getWorkOrder().getOrderId(), task.getWorkOrder().getDescription());
        } else {
          // 派发失败，重新加入队列
          dispatchQueue.offer(task);
        }
      } catch (Exception e) {
        System.err.println("派单处理异常: " + e.getMessage());
      }
    }
  }

  private boolean simulateDispatch(WorkOrder workOrder) {
    // 模拟派发逻辑（实际应调用外部系统）
    try {
      Thread.sleep(100); // 模拟网络延迟
      return ThreadLocalRandom.current().nextDouble() < 0.95; // 95%成功率
    } catch (InterruptedException e) {
      Thread.currentThread().interrupt();
      return false;
    }
  }

  private void updateStatistics(String orderType, boolean success) {
    orderStatistics.computeIfAbsent(orderType, k -> new WorkOrderStatistics())
        .recordDispatch(success);
  }

  /**
   * 计算自动派单率
   */
  public double calculateAutoGenerationRate() {
    long totalDispatches = orderStatistics.values().stream()
        .mapToLong(WorkOrderStatistics::getTotalDispatches)
        .sum();
    long autoDispatches = orderStatistics.values().stream()
        .mapToLong(WorkOrderStatistics::getAutoDispatches)
        .sum();

    return totalDispatches > 0 ? (double) autoDispatches / totalDispatches : 0.0;
  }

  /**
   * 计算处置效率
   */
  public double calculateDisposalEfficiency() {
    return orderStatistics.values().stream()
        .mapToDouble(WorkOrderStatistics::calculateEfficiency)
        .average()
        .orElse(0.0);
  }

  public int getQueueSize() {
    return dispatchQueue.size();
  }
}