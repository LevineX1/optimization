package org.example.service.model;

import lombok.Getter;

import java.time.LocalDateTime;


/**
 * 派单结果类
 */
@Getter
public class DispatchResult {
  private final boolean success;
  private final String message;
  private final String orderId;

  private final LocalDateTime timestamp;

  public DispatchResult(boolean success, String message, String orderId, LocalDateTime timestamp) {
    this.success = success;
    this.message = message;
    this.orderId = orderId;
    this.timestamp = timestamp;
  }
}