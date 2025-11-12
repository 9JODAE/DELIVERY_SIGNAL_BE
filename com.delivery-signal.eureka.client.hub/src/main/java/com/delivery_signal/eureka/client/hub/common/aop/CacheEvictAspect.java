package com.delivery_signal.eureka.client.hub.common.aop;

import java.lang.reflect.Method;
import java.util.Set;

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

import com.delivery_signal.eureka.client.hub.common.annotation.CacheEvict;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Aspect
@Component
@RequiredArgsConstructor
public class CacheEvictAspect {

	private final RedisTemplate<String, String> redisTemplate;
	private final ExpressionParser parser = new SpelExpressionParser();
	private final DefaultParameterNameDiscoverer nameDiscoverer = new DefaultParameterNameDiscoverer();

	@Around("@annotation(com.delivery_signal.eureka.client.hub.common.annotation.CacheEvict)")
	public Object handleCacheEviction(ProceedingJoinPoint joinPoint) throws Throwable {
		MethodSignature signature = (MethodSignature) joinPoint.getSignature();
		Method method = signature.getMethod();
		CacheEvict annotation = method.getAnnotation(CacheEvict.class);

		EvaluationContext context = buildEvaluationContext(method, joinPoint.getArgs());

		Object result = joinPoint.proceed();

		try {
			evictCache(annotation, context);
		} catch (Exception e) {
			log.error("[CacheEvict] 캐시 삭제 중 오류 발생 - {}", e.getMessage(), e);
		}

		return result;
	}

	private void evictCache(CacheEvict annotation, EvaluationContext context) {
		final String cachePrefix = annotation.value() + "::";

		if (annotation.allEntries()) {
			Set<String> keys = redisTemplate.keys(cachePrefix + "*");
			if (!keys.isEmpty()) {
				redisTemplate.delete(keys);
				log.info("[CacheEvict] 캐시 전체 삭제 완료 - prefix={}, count={}", cachePrefix, keys.size());
			} else {
				log.debug("[CacheEvict] 삭제할 캐시 없음 - prefix={}", cachePrefix);
			}
			return;
		}

		if (!annotation.key().isBlank()) {
			String keyValue = parser.parseExpression(annotation.key()).getValue(context, String.class);
			if (keyValue == null || keyValue.isBlank()) {
				log.warn("[CacheEvict] 키 계산 실패 - SpEL: {}", annotation.key());
				return;
			}

			String cacheKey = String.format("%s%s", cachePrefix, keyValue);
			Boolean deleted = redisTemplate.delete(cacheKey);
			if (deleted) {
				log.info("[CacheEvict] 캐시 삭제 완료 - key={}", cacheKey);
			} else {
				log.debug("[CacheEvict] 삭제할 캐시 없음 - key={}", cacheKey);
			}
		}
	}

	private EvaluationContext buildEvaluationContext(Method method, Object[] args) {
		EvaluationContext context = new StandardEvaluationContext();
		String[] paramNames = nameDiscoverer.getParameterNames(method);
		if (paramNames != null) {
			for (int i = 0; i < paramNames.length; i++) {
				context.setVariable(paramNames[i], args[i]);
			}
		}
		return context;
	}
}
