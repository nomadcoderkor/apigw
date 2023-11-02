package com.samsung.cloud.apigw.model;

import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TokenDto {

  private String token;
  private LocalDateTime expiredAt;

  public TokenDto(String token) {
    this.token = token;
  }
}
