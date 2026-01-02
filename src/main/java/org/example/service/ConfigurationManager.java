package org.example.service;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 配置管理器
 */
public class ConfigurationManager {
  private static final String CONFIG_FILE = "network_optimization.conf";
  private final Properties properties = new Properties();
  private final Map<String, ConfigListener> listeners = new ConcurrentHashMap<>();

  public ConfigurationManager() {
    loadConfiguration();
    startConfigWatch();
  }

  private void loadConfiguration() {
    try (InputStream input = getClass().getClassLoader().getResourceAsStream(CONFIG_FILE)) {
      if (input != null) {
        properties.load(input);
        System.out.println("配置文件加载成功: " + CONFIG_FILE);
      } else {
        loadDefaultConfiguration();
      }
    } catch (IOException e) {
      System.err.println("配置文件加载失败，使用默认配置: " + e.getMessage());
      loadDefaultConfiguration();
    }
  }

  private void loadDefaultConfiguration() {
    properties.setProperty("probe.interval", "30");
    properties.setProperty("stream.window.size", "15");
    properties.setProperty("stream.slide.interval", "5");
    properties.setProperty("alert.queue.size", "1000");
    properties.setProperty("model.confidence.threshold", "0.6");
  }

  private void startConfigWatch() {
    Thread watchThread = new Thread(() -> {
      while (!Thread.currentThread().isInterrupted()) {
        try {
          Thread.sleep(30000); // 每30秒检查一次配置更新
          checkConfigUpdate();
        } catch (InterruptedException e) {
          Thread.currentThread().interrupt();
          break;
        }
      }
    });
    watchThread.setDaemon(true);
    watchThread.start();
  }

  private void checkConfigUpdate() {
    // 简化的配置更新检查
    Properties newProps = new Properties();
    try (InputStream input = getClass().getClassLoader().getResourceAsStream(CONFIG_FILE)) {
      if (input != null) {
        newProps.load(input);
        if (!properties.equals(newProps)) {
          properties.clear();
          properties.putAll(newProps);
          notifyConfigChange();
        }
      }
    } catch (IOException e) {
      System.err.println("配置更新检查失败: " + e.getMessage());
    }
  }

  private void notifyConfigChange() {
    for (ConfigListener listener : listeners.values()) {
      try {
        listener.onConfigChanged(new HashMap(properties));
      } catch (Exception e) {
        System.err.println("配置变更通知失败: " + e.getMessage());
      }
    }
  }

  public String getProperty(String key) {
    return properties.getProperty(key);
  }

  public String getProperty(String key, String defaultValue) {
    return properties.getProperty(key, defaultValue);
  }

  public int getIntProperty(String key, int defaultValue) {
    try {
      return Integer.parseInt(properties.getProperty(key));
    } catch (NumberFormatException e) {
      return defaultValue;
    }
  }

  public double getDoubleProperty(String key, double defaultValue) {
    try {
      return Double.parseDouble(properties.getProperty(key));
    } catch (NumberFormatException e) {
      return defaultValue;
    }
  }

  public void addConfigListener(String name, ConfigListener listener) {
    listeners.put(name, listener);
  }

  public interface ConfigListener {
    void onConfigChanged(Map<String, String> newConfig);
  }
}