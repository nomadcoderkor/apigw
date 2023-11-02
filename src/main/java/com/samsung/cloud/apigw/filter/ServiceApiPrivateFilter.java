package com.samsung.cloud.apigw.filter;

import com.samsung.cloud.apigw.common.error.TokenErrorCode;
import com.samsung.cloud.apigw.common.exception.ApiException;
import com.samsung.cloud.apigw.model.TokenDto;
import com.samsung.cloud.apigw.model.TokenValidationRequest;
import com.samsung.cloud.apigw.model.TokenValidationResponse;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.util.UriComponentsBuilder;
import reactor.core.publisher.Mono;

@Slf4j
@Configuration
public class ServiceApiPrivateFilter extends
    AbstractGatewayFilterFactory<ServiceApiPrivateFilter.Config> {

  public ServiceApiPrivateFilter() {
    super(Config.class);
  }
//    private static Log log;
//    private static final Logger log = LoggerFactory.getLogger(ServiceApiPrivateFilter.class);

  @Bean
  public RouteLocator customRouteLocator(RouteLocatorBuilder builder) {
    return builder.routes()
        .route("service-api-private-route", r -> r
            .path("/service-api/api/**")
            .filters(f -> f.filter(this.apply(new Config())))
            .uri("http://localhost:8080")
        )
        .build();
  }

  @Override
  public GatewayFilter apply(Config config) {
    return (exchange, chain) -> {
      String uri = exchange.getRequest().getURI().toString();
      log.info("service api private filter route uri : {}", uri);

      // 토큰 유무 체크 시작
      List<String> headers = exchange.getRequest().getHeaders().get("authorization-token");
      String token = (headers != null && !headers.isEmpty()) ? headers.get(0) : null;
      log.info("authorization token : {}", token);

      if (token == null) {
        throw new ApiException(TokenErrorCode.AUTHORIZATION_TOKEN_NOT_FOUND);
      }
      log.info("Authorization token : {}", token);
      //토큰 유무 체크 끝

      // 토큰 유효성 검증
      String accountApiUrl = UriComponentsBuilder.fromUriString("http://localhost")
          .port(5000)
//          .path("/internal-api/token/validation")
          .build()
          .encode()
          .toUriString();

      WebClient webClient = WebClient.builder().baseUrl(accountApiUrl).build();

      TokenValidationRequest request = new TokenValidationRequest(new TokenDto(token));

//      return webClient
//          .post()
//          .body(Mono.just(request), TokenValidationRequest.class)
//          .accept(MediaType.APPLICATION_JSON)
//          .retrieve()
//          .onStatus(
//            status -> status.isError(),
//            response -> response.bodyToMono(new ParameterizedTypeReference<Object>() {
//            })
//                .f
//      )

      return webClient
          .post()
          .body(Mono.just(request), TokenValidationRequest.class)
          .accept(MediaType.APPLICATION_JSON)
          .retrieve()
          .onStatus(status -> status.isError(),
              response -> response.bodyToMono(new ParameterizedTypeReference<Object>() {
                  })
                  .flatMap(error -> {
                    log.error("", error);
                    return Mono.error(new ApiException(TokenErrorCode.TOKEN_EXCEPTION));
                  })
          )
          .bodyToMono(new ParameterizedTypeReference<TokenValidationResponse>() {
          })
          .flatMap(response -> {
            log.info("response : {}", response);
            String userId = (response.getUserId() != null) ? response.getUserId().toString() : null;
            exchange.getRequest().mutate()
                .header("x-user-id", userId)
                .build();
            return chain.filter(exchange.mutate().request(exchange.getRequest()).build());
          })
          .onErrorMap(e -> {
            log.error("", e);
            return e;
          });
//      return null;
    };
  }

  public static class Config {

  }

}
