package com.atlassian_cinema_screen;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@RequiredArgsConstructor
public class Interval {

  private int start;
  private int end; // exclusive

  Interval(int start, int end) {
    this.start = start;
    this.end = end;
  }
}