package com.samsung.cloud.apigw.route;

import com.samsung.cloud.apigw.filter.ServiceApiPrivateFilter;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.cloud.gateway.route.Route;
import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

@Configuration
public class RouteConfig {

  private final ServiceApiPrivateFilter serviceApiPrivateFilter;

  public RouteConfig(ServiceApiPrivateFilter serviceApiPrivateFilter) {
    this.serviceApiPrivateFilter = serviceApiPrivateFilter;
  }

  @Bean
  public RouteLocator gatewayRoutes(RouteLocatorBuilder builder) {
    return builder.routes()
        .route("public-service-api", r -> r
            .order(0)
            .path("/open-api/hcms/api/**")
            .filters(f -> f
                .filter(serviceApiPrivateFilter.apply(new ServiceApiPrivateFilter.Config()))
                .rewritePath("/open-api/hcms(?<segment>/?.*)", "${segment}")
            )
            .uri("http://localhost:8080")
        )
        .route("private-service-api", r -> r
            .order(1)
            .path("/open-api/hcms/api/**")
            .filters(f -> f
                .filter(serviceApiPrivateFilter.apply(new ServiceApiPrivateFilter.Config()))
                .rewritePath("/open-api/hcms(?<segment>/?.*)", "${segment}")
            )
            .uri("http://localhost:8080")
        )
        .build();
  }
}
