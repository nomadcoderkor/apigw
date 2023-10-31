package com.samsung.cloud.apigw.common;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public interface Log {
  default Logger getLog() {
    return LoggerFactory.getLogger(this.getClass());
  }

  void info(String s, String uri);
}
