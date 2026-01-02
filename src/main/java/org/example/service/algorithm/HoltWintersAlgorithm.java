package org.example.service.algorithm;


import org.example.service.model.ForecastResult;

/**
 * Holt-Winters三次指数平滑算法实现
 * 用于动态阈值计算和时序预测
 */
public class HoltWintersAlgorithm {
  private final double alpha; // 水平平滑参数
  private final double beta;  // 趋势平滑参数
  private final double gamma; // 季节性平滑参数
  private final int seasonality; // 季节性周期

  public HoltWintersAlgorithm(double alpha, double beta, double gamma, int seasonality) {
    this.alpha = alpha;
    this.beta = beta;
    this.gamma = gamma;
    this.seasonality = seasonality;
  }

  public ForecastResult forecast(double[] historicalData) {
    if (historicalData.length < seasonality * 2) {
      throw new IllegalArgumentException("历史数据长度不足以进行季节性分析");
    }

    int n = historicalData.length;
    double[] level = new double[n];
    double[] trend = new double[n];
    double[] seasonal = new double[n];
    double[] forecast = new double[n];

    // 初始化
    initializeComponents(historicalData, level, trend, seasonal);

    // 迭代计算
    for (int i = seasonality; i < n; i++) {
      level[i] = alpha * (historicalData[i] - seasonal[i - seasonality])
          + (1 - alpha) * (level[i - 1] + trend[i - 1]);
      trend[i] = beta * (level[i] - level[i - 1]) + (1 - beta) * trend[i - 1];
      seasonal[i] = gamma * (historicalData[i] - level[i])
          + (1 - gamma) * seasonal[i - seasonality];
      forecast[i] = level[i - 1] + trend[i - 1] + seasonal[i - seasonality];
    }

    double nextForecast = level[n - 1] + trend[n - 1] + seasonal[n - seasonality];
    double dynamicThreshold = calculateDynamicThreshold(historicalData, forecast);

    return new ForecastResult(nextForecast, dynamicThreshold, calculateErrorRate(historicalData
        , forecast));
  }

  private void initializeComponents(double[] data, double[] level, double[] trend,
                                    double[] seasonal) {
    // 简单的初始化逻辑
    for (int i = 0; i < seasonality; i++) {
      seasonal[i] = data[i] / calculateSeasonalAverage(data, i, seasonality);
    }

    level[0] = data[0];
    trend[0] = 0;
  }

  private double calculateSeasonalAverage(double[] data, int position, int seasonality) {
    double sum = 0;
    int count = 0;
    for (int i = position; i < data.length; i += seasonality) {
      sum += data[i];
      count++;
    }
    return sum / count;
  }

  private double calculateDynamicThreshold(double[] actual, double[] forecast) {
    double mae = 0;
    int count = 0;
    for (int i = seasonality; i < actual.length; i++) {
      mae += Math.abs(actual[i] - forecast[i]);
      count++;
    }
    mae /= count;
    return mae * 3; // 3倍MAE作为动态阈值
  }

  private double calculateErrorRate(double[] actual, double[] forecast) {
    double sumSquaredError = 0;
    double sumActual = 0;
    for (int i = seasonality; i < actual.length; i++) {
      sumSquaredError += Math.pow(actual[i] - forecast[i], 2);
      sumActual += actual[i];
    }
    return Math.sqrt(sumSquaredError / (actual.length - seasonality)) / (sumActual / (actual.length - seasonality));
  }
}