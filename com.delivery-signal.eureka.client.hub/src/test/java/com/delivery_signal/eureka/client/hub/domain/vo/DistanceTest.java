package com.delivery_signal.eureka.client.hub.domain.vo;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class DistanceTest {

	@Test
	@DisplayName("정상: 유효한 거리로 생성")
	void createValidDistance() {
		Distance d = Distance.of(0.0); // 최소 경계 포함
		assertNotNull(d);

		Distance d2 = Distance.of(1000.5); // 일반 값
		assertNotNull(d2);

		Distance d3 = Distance.of(10000.0); // 최대 경계 포함
		assertNotNull(d3);
	}

	@Test
	@DisplayName("예외: 음수 거리 입력 시 IllegalArgumentException")
	void negativeDistanceThrows() {
		assertThrows(IllegalArgumentException.class, () -> Distance.of(-0.1));
	}

	@Test
	@DisplayName("예외: 최대값 초과 시 IllegalArgumentException")
	void aboveMaxDistanceThrows() {
		assertThrows(IllegalArgumentException.class, () -> Distance.of(10000.0001));
	}
}