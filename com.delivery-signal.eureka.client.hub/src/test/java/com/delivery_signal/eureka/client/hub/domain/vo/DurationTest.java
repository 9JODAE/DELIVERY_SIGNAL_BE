package com.delivery_signal.eureka.client.hub.domain.vo;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class DurationTest {

	@Test
	@DisplayName("정상: 유효한 소요 시간으로 생성")
	void createValidDuration() {
		Duration d1 = Duration.of(0);    // 최소 경계 포함
		assertNotNull(d1);

		Duration d2 = Duration.of(30);   // 일반 값
		assertNotNull(d2);
	}

	@Test
	@DisplayName("예외: 음수 소요 시간 입력 시 IllegalArgumentException")
	void negativeDurationThrows() {
		assertThrows(IllegalArgumentException.class, () -> Duration.of(-1));
		assertThrows(IllegalArgumentException.class, () -> Duration.of(-10));
	}
}