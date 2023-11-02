package com.samsung.cloud.apigw.model;

import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;

@JsonNaming(PropertyNamingStrategy.SnakeCaseStrategy.class)
public class TokenValidationRequest {

  private TokenDto tokenDto;

  public TokenValidationRequest(TokenDto tokenDto) {
    this.tokenDto = tokenDto;
  }

  public TokenDto getTokenDto() {
    return tokenDto;
  }

  public void setTokenDto(TokenDto tokenDto) {
    this.tokenDto = tokenDto;
  }
}