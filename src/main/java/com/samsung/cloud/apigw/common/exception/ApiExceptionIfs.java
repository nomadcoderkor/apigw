package com.samsung.cloud.apigw.common.exception;


import com.samsung.cloud.apigw.common.error.ErrorCodeIfs;

public interface ApiExceptionIfs {

  ErrorCodeIfs getErrorCodeIfs();

  String getErrorDescription();
}
