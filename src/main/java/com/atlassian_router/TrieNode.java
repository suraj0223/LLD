package com.atlassian_router;

import java.util.HashMap;
import java.util.Map;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TrieNode {

  private final Map<String, TrieNode> children;
  private String result;
  private boolean isEndOfPath;

  public TrieNode() {
    this.children = new HashMap<>();
    this.isEndOfPath = false;
  }
}
