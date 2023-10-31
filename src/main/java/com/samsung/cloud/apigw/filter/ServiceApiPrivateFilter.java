package com.samsung.cloud.apigw.filter;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

@Component
public class ServiceApiPrivateFilter extends AbstractGatewayFilterFactory<ServiceApiPrivateFilter.Config> {

//    private static Log log;
    private static final Logger log = LoggerFactory.getLogger(ServiceApiPrivateFilter.class);

    public ServiceApiPrivateFilter() {
        super(Config.class);
    }

    public static class Config {
    }

    @Override
    public GatewayFilter apply(Config config) {
        return (exchange, chain) -> {
            String uri = exchange.getRequest().getURI().toString();
            log.info("service api private filter route uri: {}", uri);

            Mono<Void> mono = chain.filter(exchange);
            return mono;
        };
    }
}
