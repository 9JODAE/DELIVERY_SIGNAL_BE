package com.delivery_signal.eureka.client.hub.common.aop;

import java.lang.reflect.Method;
import java.lang.reflect.Type;
import java.time.Duration;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.core.DefaultParameterNameDiscoverer;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.expression.EvaluationContext;
import org.springframework.expression.ExpressionParser;
import org.springframework.expression.spel.standard.SpelExpressionParser;
import org.springframework.expression.spel.support.StandardEvaluationContext;
import org.springframework.stereotype.Component;

import com.delivery_signal.eureka.client.hub.common.annotation.Cacheable;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Aspect
@Component
@RequiredArgsConstructor
public class CacheAspect {

	private final RedisTemplate<String, String> redisTemplate;
	private final ObjectMapper objectMapper;

	private final ExpressionParser parser = new SpelExpressionParser();
	private final DefaultParameterNameDiscoverer nameDiscoverer = new DefaultParameterNameDiscoverer();

	@Around("@annotation(com.delivery_signal.eureka.client.hub.common.annotation.Cacheable)")
	public Object handleCache(ProceedingJoinPoint joinPoint) throws Throwable {
		MethodSignature signature = (MethodSignature) joinPoint.getSignature();
		Method method = signature.getMethod();
		Cacheable annotation = method.getAnnotation(Cacheable.class);

		String cacheKey = buildCacheKey(method, joinPoint.getArgs(), annotation);

		Type genericReturnType = method.getGenericReturnType();
		JavaType javaType = objectMapper.getTypeFactory().constructType(genericReturnType);

		Object cachedResult = getFromCache(cacheKey, javaType);
		if (cachedResult != null) {
			return cachedResult;
		}

		Object result = joinPoint.proceed();

		saveToCache(cacheKey, result, annotation.ttl());

		return result;
	}

	private String buildCacheKey(Method method, Object[] args, Cacheable annotation) {
		EvaluationContext context = new StandardEvaluationContext();
		String[] paramNames = nameDiscoverer.getParameterNames(method);
		if (paramNames != null) {
			for (int i = 0; i < paramNames.length; i++) {
				context.setVariable(paramNames[i], args[i]);
			}
		}
		String keyValue = parser.parseExpression(annotation.key()).getValue(context, String.class);
		String cacheKey = annotation.value() + "::" + keyValue;
		log.debug("캐시 키 생성: {}", cacheKey);
		return cacheKey;
	}

	private Object getFromCache(String cacheKey, JavaType javaType) {
		String cached = redisTemplate.opsForValue().get(cacheKey);
		if (cached == null) return null;

		try {
			log.debug("캐시 히트: {}", cacheKey);
			return objectMapper.readValue(cached, javaType);
		} catch (JsonProcessingException e) {
			log.warn("캐시 역직렬화 실패 → 캐시 삭제: {}", cacheKey, e);
			redisTemplate.delete(cacheKey);
			return null;
		}
	}

	private void saveToCache(String cacheKey, Object value, long ttlHours) {
		try {
			String json = objectMapper.writeValueAsString(value);
			redisTemplate.opsForValue().set(cacheKey, json, Duration.ofHours(ttlHours));
			log.debug("캐시 저장 완료: {}", cacheKey);
		} catch (JsonProcessingException e) {
			log.error("캐시 직렬화 실패: {}", cacheKey, e);
			redisTemplate.delete(cacheKey);
		}
	}
}
