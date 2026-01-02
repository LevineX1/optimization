package org.example.service.model;

import com.alibaba.fastjson.JSONObject;

import java.time.LocalDateTime;

/**
 * 系统运行报告
 */
public class SystemReport {
  private final PerformanceReport performanceReport;
  private final ModelPerformance modelPerformance;
  private final double topologyAccuracy;
  private final double autoGenerationRate;
  private final double disposalEfficiency;
  private final LocalDateTime generateTime;

  public SystemReport(PerformanceReport performanceReport, ModelPerformance modelPerformance,
                      double topologyAccuracy, double autoGenerationRate,
                      double disposalEfficiency, LocalDateTime generateTime) {
    this.performanceReport = performanceReport;
    this.modelPerformance = modelPerformance;
    this.topologyAccuracy = topologyAccuracy;
    this.autoGenerationRate = autoGenerationRate;
    this.disposalEfficiency = disposalEfficiency;
    this.generateTime = generateTime;
  }

  public JSONObject toJson() {
    JSONObject json = new JSONObject();
    json.put("generateTime", generateTime.toString());
    json.put("topologyAccuracy", topologyAccuracy);
    json.put("autoGenerationRate", autoGenerationRate);
    json.put("disposalEfficiency", disposalEfficiency);
    json.put("performance", performanceReport.toJson());

    JSONObject modelJson = new JSONObject();
    modelJson.put("trainingAccuracy", modelPerformance.getTrainingAccuracy());
    modelJson.put("testAccuracy", modelPerformance.getTestAccuracy());
    modelJson.put("inferenceSpeed", modelPerformance.getInferenceSpeed());
    json.put("modelPerformance", modelJson);

    return json;
  }
}