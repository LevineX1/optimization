package org.example.service.bigdata;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import org.example.service.model.NetworkNode;
import org.example.service.model.PerformanceMetrics;

import java.util.Collections;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Map;
import java.util.Queue;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 拓扑分析引擎 - 网络拓扑关系分析和影响域计算
 */
public class TopologyAnalysisEngine {
  private final Map<String, NetworkNode> topologyGraph = new ConcurrentHashMap<>();
  private final Map<String, Set<String>> adjacencyList = new ConcurrentHashMap<>();
  private final PerformanceMetrics performanceMetrics = new PerformanceMetrics();

  public TopologyAnalysisEngine() {
    initializeDefaultTopology();
  }

  private void initializeDefaultTopology() {
    // 初始化默认拓扑结构（简化实现）
    addNetworkNode(new NetworkNode("BS_001", "基站", "核心城区", 34.261, 117.185));
    addNetworkNode(new NetworkNode("BS_002", "基站", "东开发区", 34.268, 117.201));
    addNetworkNode(new NetworkNode("RRU_001", "射频单元", "核心城区", 34.261, 117.185));
    addNetworkNode(new NetworkNode("RRU_002", "射频单元", "东开发区", 34.268, 117.201));

    // 建立连接关系
    addConnection("BS_001", "RRU_001");
    addConnection("BS_002", "RRU_002");
    addConnection("BS_001", "BS_002"); // 基站间互联
  }

  public void addNetworkNode(NetworkNode node) {
    topologyGraph.put(node.getNodeId(), node);
    adjacencyList.putIfAbsent(node.getNodeId(), new HashSet<>());
  }

  public void addConnection(String nodeId1, String nodeId2) {
    adjacencyList.computeIfAbsent(nodeId1, k -> new HashSet<>()).add(nodeId2);
    adjacencyList.computeIfAbsent(nodeId2, k -> new HashSet<>()).add(nodeId1);
  }

  /**
   * 计算故障影响域
   */
  public Set<String> calculateImpactDomain(String faultyNodeId) {
    long startTime = System.currentTimeMillis();
    boolean success = false;

    try {
      if (!topologyGraph.containsKey(faultyNodeId)) {
        throw new IllegalArgumentException("未知节点: " + faultyNodeId);
      }

      Set<String> impactDomain = new HashSet<>();
      Queue<String> queue = new LinkedList<>();
      Set<String> visited = new HashSet<>();

      queue.offer(faultyNodeId);
      visited.add(faultyNodeId);

      // BFS遍历拓扑图，找出所有受影响节点
      while (!queue.isEmpty()) {
        String currentNode = queue.poll();
        impactDomain.add(currentNode);

        Set<String> neighbors = adjacencyList.get(currentNode);
        if (neighbors != null) {
          for (String neighbor : neighbors) {
            if (!visited.contains(neighbor)) {
              visited.add(neighbor);
              queue.offer(neighbor);
            }
          }
        }
      }

      success = true;
      return Collections.unmodifiableSet(impactDomain);

    } finally {
      long duration = System.currentTimeMillis() - startTime;
      performanceMetrics.recordRequestEnd(duration, success, null);
    }
  }

  /**
   * 分析拓扑准确性
   */
  public double analyzeTopologyAccuracy() {
    int totalConnections = adjacencyList.values().stream()
        .mapToInt(Set::size)
        .sum() / 2; // 无向图，连接数除以2

    int verifiedConnections = verifyTopologyConnections();
    return totalConnections > 0 ? (double) verifiedConnections / totalConnections : 1.0;
  }

  private int verifyTopologyConnections() {
    // 简化的拓扑验证逻辑
    int verified = 0;
    for (Map.Entry<String, Set<String>> entry : adjacencyList.entrySet()) {
      String nodeId = entry.getKey();
      for (String neighbor : entry.getValue()) {
        // 检查反向连接是否存在
        if (adjacencyList.get(neighbor).contains(nodeId)) {
          verified++;
        }
      }
    }
    return verified / 2; // 无向图，验证的连接数除以2
  }

  /**
   * 获取节点详细信息
   */
  public NetworkNode getNodeInfo(String nodeId) {
    return topologyGraph.get(nodeId);
  }

  /**
   * 导出拓扑图为JSON格式
   */
  public JSONObject exportTopology() {
    JSONObject topologyJson = new JSONObject();

    JSONArray nodesArray = new JSONArray();
    for (NetworkNode node : topologyGraph.values()) {
      JSONObject nodeJson = new JSONObject();
      nodeJson.put("id", node.getNodeId());
      nodeJson.put("type", node.getNodeType());
      nodeJson.put("location", node.getLocation());
      nodeJson.put("latitude", node.getLatitude());
      nodeJson.put("longitude", node.getLongitude());
      nodesArray.add(nodeJson);
    }
    topologyJson.put("nodes", nodesArray);

    JSONArray edgesArray = new JSONArray();
    for (Map.Entry<String, Set<String>> entry : adjacencyList.entrySet()) {
      String source = entry.getKey();
      for (String target : entry.getValue()) {
        if (source.compareTo(target) < 0) { // 避免重复边
          JSONObject edgeJson = new JSONObject();
          edgeJson.put("source", source);
          edgeJson.put("target", target);
          edgesArray.add(edgeJson);
        }
      }
    }
    topologyJson.put("edges", edgesArray);

    return topologyJson;
  }
}