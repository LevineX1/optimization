package org.example.service.tools;



import org.example.service.model.log.LogEntry;
import org.example.service.model.log.LogLevel;

import java.util.Map;
import java.util.Queue;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * 日志管理器
 */
public class Logger {
  private static final Map<String, Logger> instances = new ConcurrentHashMap<>();
  private final String name;
  private final Queue<LogEntry> logQueue = new ConcurrentLinkedQueue<>();
  private final ScheduledExecutorService logFlusher =
      Executors.newSingleThreadScheduledExecutor();

  private Logger(String name) {
    this.name = name;
    startLogFlusher();
  }

  public static Logger getLogger(String name) {
    return instances.computeIfAbsent(name, Logger::new);
  }

  public static Logger getLogger(Class<?> clazz) {
    return getLogger(clazz.getSimpleName());
  }

  private void startLogFlusher() {
    logFlusher.scheduleAtFixedRate(this::flushLogs, 1, 1, TimeUnit.SECONDS);
  }

  public void info(String message) {
    log(LogLevel.INFO, message, null);
  }

  public void warn(String message) {
    log(LogLevel.WARN, message, null);
  }

  public void error(String message, Throwable throwable) {
    log(LogLevel.ERROR, message, throwable);
  }

  public void debug(String message) {
    log(LogLevel.DEBUG, message, null);
  }

  private void log(LogLevel level, String message, Throwable throwable) {
    LogEntry logEntry = new LogEntry(name, level, message, throwable, System.currentTimeMillis());
    logQueue.offer(logEntry);

    // 控制台输出（简化实现）
    System.out.printf("[%s] %s %s: %s%n",
        logEntry.getFormattedTime(), level, name, message);
    if (throwable != null) {
      throwable.printStackTrace();
    }
  }

  private void flushLogs() {
    // 实际应用中应该将日志写入文件或发送到日志服务器
    logQueue.clear(); // 简化实现：清空队列
  }

  public void shutdown() {
    logFlusher.shutdown();
    try {
      if (!logFlusher.awaitTermination(5, TimeUnit.SECONDS)) {
        logFlusher.shutdownNow();
      }
    } catch (InterruptedException e) {
      logFlusher.shutdownNow();
      Thread.currentThread().interrupt();
    }
  }
}