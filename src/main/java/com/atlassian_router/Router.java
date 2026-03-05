package com.atlassian_router;

import java.util.List;
import java.util.Map.Entry;

public class Router {

  private final TrieNode root;

  public Router() {
    this.root = new TrieNode();
  }


  private boolean match(TrieNode currentNode, List<String> parts, int index, StringBuilder result) {
    if (index == parts.size()) {
      if (currentNode.isEndOfPath()) {
        result.append(currentNode.getResult());
        return true;
      }
      return false;
    }

    String currPartToMatch = parts.get(index);

    // 1. Exact match (try exact match first for more specific routes)
    if (currentNode.getChildren().containsKey(currPartToMatch)) {
      if (match(currentNode.getChildren().get(currPartToMatch), parts, index + 1, result)) {
        return true;
      }
    }

    // 2. WildCard pattern (/*) - greedy matching: try consuming 0, 1, 2, ... segments
    if (currentNode.getChildren().containsKey("*")) {
      TrieNode wildcardChild = currentNode.getChildren().get("*");

      // Try matching from the wildcard child node, consuming segments greedily
      // Start from current index (consuming current segment) and try all possibilities
      for (int i = index; i <= parts.size(); i++) {
        if (match(wildcardChild, parts, i, result)) {
          return true;
        }
      }
    }

    // 3. Path params (/:param)
    for (Entry<String, TrieNode> child : currentNode.getChildren().entrySet()) {
      if (child.getKey().startsWith(":")) {
        // Use child.getValue() directly instead of redundant HashMap lookup
        if (match(child.getValue(), parts, index + 1, result)) {
          return true;
        }
      }
    }

    return false;
  }

  public void addRoute(String path, String result) {
    List<String> parts = List.of(path.split("/"));
    TrieNode curr = root;

    for (String part : parts) {
      curr.getChildren().putIfAbsent(part, new TrieNode());
      curr = curr.getChildren().get(part);
    }
    curr.setEndOfPath(true);
    curr.setResult(result);
  }

  public String callRoute(String path) {
    List<String> parts = List.of(path.split("/"));
    StringBuilder result = new StringBuilder();

    if (match(root, parts, 0, result)) {
      return result.toString();
    }

    return "404 Not Found!";
  }
}
