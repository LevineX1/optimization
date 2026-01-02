package org.example.service.algorithm;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * 随机森林简化实现（用于XGBoost模拟）
 */
public class RandomForest {
  private final List<DecisionTree> trees;
  private final int numTrees;

  public RandomForest(int numTrees, int numFeatures) {
    this.numTrees = numTrees;
    this.trees = new ArrayList<>();
    for (int i = 0; i < numTrees; i++) {
      trees.add(new DecisionTree(numFeatures));
    }
  }

  public double predict(Map<String, Double> features) {
    double sum = 0;
    for (DecisionTree tree : trees) {
      sum += tree.predict(features);
    }
    return sum / trees.size();
  }

  public void update(Map<String, Double> features, double target) {
    // 简化更新逻辑
    for (DecisionTree tree : trees) {
      if (Math.random() > 0.5) { // 随机选择部分树更新
        tree.partialUpdate(features, target);
      }
    }
  }
}