package org.example.service.model.log;

import lombok.Data;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;

/**
 * 日志条目类
 */
@Data
public class LogEntry {
  private final String loggerName;
  private final LogLevel level;
  private final String message;
  private final Throwable throwable;
  private final long timestamp;

  public LogEntry(String loggerName, LogLevel level, String message,
                  Throwable throwable, long timestamp) {
    this.loggerName = loggerName;
    this.level = level;
    this.message = message;
    this.throwable = throwable;
    this.timestamp = timestamp;
  }

  public String getFormattedTime() {
    return LocalDateTime.ofInstant(Instant.ofEpochMilli(timestamp), ZoneId.systemDefault())
        .format(DateTimeFormatter.ISO_LOCAL_DATE_TIME);
  }
}