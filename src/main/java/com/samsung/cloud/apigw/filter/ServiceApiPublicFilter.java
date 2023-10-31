package com.samsung.cloud.apigw.filter;

import com.samsung.cloud.apigw.common.Log;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

@Component
public class ServiceApiPublicFilter extends AbstractGatewayFilterFactory<ServiceApiPublicFilter.Config> {

//    private static Log log;
    private static final Logger log = LoggerFactory.getLogger(ServiceApiPublicFilter.class);

    public ServiceApiPublicFilter() {
        super(Config.class);
    }

    public static class Config {
    }

    @Override
    public GatewayFilter apply(Config config) {
        return (exchange, chain) -> {
            String uri = exchange.getRequest().getURI().toString();
            log.info("service api public filter route uri: {}", uri);

            Mono<Void> mono = chain.filter(exchange);
            return mono;
        };
    }
}
