package com.example.lms_api_gateway.security.jwt;

import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import reactor.core.publisher.Mono;

import java.util.List;

@Component
public class JwtAuthenticationFilter implements GlobalFilter, Ordered {

    private final JwtUtil jwtUtil;

    public JwtAuthenticationFilter(JwtUtil jwtUtil) {
        this.jwtUtil = jwtUtil;
    }


    private static final List<String> PUBLIC_URLS = List.of(
            "/api/auth/login",
            "/api/auth/register",
            "/api/chat",
            "/api/messages"
    );

    @Override
    public Mono<Void> filter(ServerWebExchange exchange,
                             GatewayFilterChain chain) {

        String path = exchange.getRequest()
                .getURI()
                .getPath();


        if (PUBLIC_URLS.stream().anyMatch(path::startsWith)) {
            return chain.filter(exchange);
        }


        String authHeader = exchange.getRequest()
                .getHeaders()
                .getFirst(HttpHeaders.AUTHORIZATION);


        if (authHeader == null || !authHeader.startsWith("Bearer ")) {

            exchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);
            return exchange.getResponse().setComplete();
        }

        String token = authHeader.substring(7);


        if (!jwtUtil.validateToken(token)) {

            exchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);
            return exchange.getResponse().setComplete();
        }


        Long userId = jwtUtil.extractUserId(token);
        String role = jwtUtil.extractRole(token);
       //s String path = exchange.getRequest().getURI().getPath();
        System.out.println("UserId = " + userId);
        System.out.println("Role = " + role);


        if (path.startsWith("/api/teacher") && !role.contains("TEACHER")) {

            exchange.getResponse().setStatusCode(HttpStatus.FORBIDDEN);
            return exchange.getResponse().setComplete();
        }
        if (path.startsWith("/api/admin") && !role.contains("ADMIN")) {

            exchange.getResponse().setStatusCode(HttpStatus.FORBIDDEN);
            return exchange.getResponse().setComplete();
        }


        if (path.startsWith("/api/student") && !role.contains("STUDENT")) {

            exchange.getResponse().setStatusCode(HttpStatus.FORBIDDEN);
            return exchange.getResponse().setComplete();
        }




        ServerHttpRequest mutatedRequest =
                exchange.getRequest()
                        .mutate()
                        .header("X-User-Id", String.valueOf(userId))
                        .header("X-User-Role", role)
                        .build();

        ServerWebExchange mutatedExchange =
                exchange.mutate()
                        .request(mutatedRequest)
                        .build();


        System.out.println("JWT FILTER HIT");

        //Long userId = jwtUtil.extractUserId(token);
        //String role = jwtUtil.extractRole(token);



        return chain.filter(mutatedExchange);

    }


    @Override
    public int getOrder() {
        return -1;
    }
}
