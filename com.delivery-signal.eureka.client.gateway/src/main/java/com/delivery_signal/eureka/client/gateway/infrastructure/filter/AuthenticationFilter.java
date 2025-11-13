package com.delivery_signal.eureka.client.gateway.infrastructure.filter;

import java.util.List;

import javax.crypto.SecretKey;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;

import com.delivery_signal.eureka.client.gateway.global.exception.EmptyClaimsException;
import com.delivery_signal.eureka.client.gateway.global.exception.ExpiredException;
import com.delivery_signal.eureka.client.gateway.global.exception.InvalidSignatureException;
import com.delivery_signal.eureka.client.gateway.global.exception.MalFormedException;
import com.delivery_signal.eureka.client.gateway.global.exception.UnsupportedException;
import com.delivery_signal.eureka.client.gateway.global.exception.errorcode.TokenErrorCode;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtParser;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.UnsupportedJwtException;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.security.SignatureException;
import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Mono;

@Slf4j
@Component
public class AuthenticationFilter implements GlobalFilter, Ordered {

	private static final List<String> PUBLIC_API_PREFIXES = List.of("/open-api/", "/v3/api-docs");
	private static final String BEARER_PREFIX = "Bearer ";
	private static final int AUTH_HEADER_BEGIN_INDEX = BEARER_PREFIX.length();

	@Value("${jwt.secret-key}")
	private String secretKey;

	@Override
	public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
		String path = exchange.getRequest().getURI().getPath();

		boolean isPublic = PUBLIC_API_PREFIXES.stream()
			.anyMatch(path::contains);
		
		if (isPublic) {
			return chain.filter(exchange);
		}

		return filterPrivateApi(exchange, chain);
	}

	private Mono<Void> filterPrivateApi(ServerWebExchange exchange, GatewayFilterChain chain) {
		String authHeader = getAuthHeader(exchange);
		if (authHeader == null || !authHeader.startsWith(BEARER_PREFIX)) {
			exchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);
			return exchange.getResponse().setComplete();
		}

		String accessToken = authHeader.substring(AUTH_HEADER_BEGIN_INDEX);
		return validateToken(exchange, chain, accessToken);
	}

	private String getAuthHeader(ServerWebExchange exchange) {
		return exchange.getRequest().getHeaders().getFirst(HttpHeaders.AUTHORIZATION);
	}

	private Mono<Void> validateToken(ServerWebExchange exchange, GatewayFilterChain chain, String accessToken) {
		Claims claims = validationTokenWithThrow(accessToken);
		String userId = claims.get("user_id", String.class);

		ServerHttpRequest request = exchange.getRequest().mutate()
            .header("x-user-id", userId)
			.build();

		return chain.filter(exchange.mutate().request(request).build());
	}

	private Claims validationTokenWithThrow(String token) {
		SecretKey key = Keys.hmacShaKeyFor(Decoders.BASE64URL.decode(secretKey));
		JwtParser parser = Jwts.parser().verifyWith(key).build();

		try {
			return parser.parseSignedClaims(token).getPayload();
		} catch (SignatureException e) {
			throw new InvalidSignatureException(TokenErrorCode.INVALID_SIGNATURE);
		} catch (MalformedJwtException e) {
			throw new MalFormedException(TokenErrorCode.MALFORMED);
		} catch (ExpiredJwtException e) {
			throw new ExpiredException(TokenErrorCode.EXPIRED);
		} catch (UnsupportedJwtException e) {
			throw new UnsupportedException(TokenErrorCode.UNSUPPORTED);
		} catch (IllegalArgumentException e) {
			throw new EmptyClaimsException(TokenErrorCode.INVALID_CLAIMS);
		}
	}

	@Override
	public int getOrder() {
		return -1;
	}
}