package org.example.service.tools;



import org.example.service.model.FaultEvent;
import org.example.service.model.PerformanceMetrics;

import javax.swing.*;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.time.LocalDateTime;
import java.util.List;

/**
 * 可视化界面生成器
 */
public class VisualizationGenerator extends JPanel {
  private final List<FaultEvent> events;
  private final PerformanceMetrics metrics;

  public VisualizationGenerator(List<FaultEvent> events, PerformanceMetrics metrics) {
    this.events = events;
    this.metrics = metrics;
    setPreferredSize(new Dimension(1200, 800));
    setBackground(Color.WHITE);
  }

  @Override
  protected void paintComponent(Graphics g) {
    super.paintComponent(g);
    Graphics2D g2d = (Graphics2D) g;
    g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

    // 绘制标题
    drawTitle(g2d);

    // 绘制性能指标面板
    drawMetricsPanel(g2d);


    // 绘制时序趋势图
    drawTimeSeriesChart(g2d);

    // 绘制地理分布图
    drawGeographicMap(g2d);
  }

  private void drawTitle(Graphics2D g2d) {
    g2d.setFont(new Font("微软雅黑", Font.BOLD, 24));
    g2d.setColor(new Color(0, 100, 200));
    g2d.drawString("无线设备障碍主动分析定位系统 - 实时监控大屏", 50, 50);

    g2d.setFont(new Font("微软雅黑", Font.PLAIN, 14));
    g2d.setColor(Color.GRAY);
    g2d.drawString("更新时间: " + LocalDateTime.now(), 50, 80);
  }

  private void drawMetricsPanel(Graphics2D g2d) {
    // 绘制指标卡片背景
    g2d.setColor(new Color(240, 245, 255));
    g2d.fillRoundRect(50, 100, 1100, 100, 15, 15);
    g2d.setColor(new Color(200, 220, 255));
    g2d.drawRoundRect(50, 100, 1100, 100, 15, 15);

    // 绘制各项指标
    String[] labels = {"总请求数", "成功率", "平均处理时间"};
    String[] values = {
        String.valueOf(metrics.getTotalRequests()),
        String.format("%.2f%%", metrics.getSuccessRate()),
        String.format("%.2fms", metrics.getAverageProcessingTime())
    };

    g2d.setFont(new Font("微软雅黑", Font.BOLD, 16));
    g2d.setColor(Color.BLACK);

    for (int i = 0; i < labels.length; i++) {
      int x = 100 + i * 220;
      g2d.drawString(labels[i], x, 130);
      g2d.setColor(new Color(0, 120, 215));
      g2d.drawString(values[i], x, 160);
      g2d.setColor(Color.BLACK);
    }
  }


  private void drawTimeSeriesChart(Graphics2D g2d) {
    // 简化实现时序图表
    g2d.setColor(new Color(240, 240, 240));
    g2d.fillRect(50, 450, 500, 300);
    g2d.setColor(Color.GRAY);
    g2d.drawRect(50, 450, 500, 300);

    g2d.setFont(new Font("微软雅黑", Font.BOLD, 16));
    g2d.setColor(Color.BLACK);
    g2d.drawString("性能指标时序图", 50, 440);
  }

  private void drawGeographicMap(Graphics2D g2d) {
    // 简化实现地理分布图
    g2d.setColor(new Color(240, 240, 240));
    g2d.fillRect(600, 450, 550, 300);
    g2d.setColor(Color.GRAY);
    g2d.drawRect(600, 450, 550, 300);

    g2d.setFont(new Font("微软雅黑", Font.BOLD, 16));
    g2d.setColor(Color.BLACK);
    g2d.drawString("基站地理分布", 600, 440);
  }

  public BufferedImage generateDashboardImage() {
    BufferedImage image = new BufferedImage(1200, 800, BufferedImage.TYPE_INT_RGB);
    Graphics2D g2d = image.createGraphics();
    paintComponent(g2d);
    g2d.dispose();
    return image;
  }
}