package com.delivery_signal.eureka.client.hub.domain.vo;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class CoordinateTest {

	@Test
	@DisplayName("정상: 유효한 위도/경도로 생성")
	void createWithFactoryMethod() {
		Coordinate c = Coordinate.of(35.5, 125.5);
		assertNotNull(c);
	}

	@Test
	@DisplayName("경계: 최소 위도 허용 (33.0)")
	void minLatitudeAllowed() {
		Coordinate c = Coordinate.of(33.0, 126.0);
		assertNotNull(c);
	}

	@Test
	@DisplayName("경계: 최대 위도 허용 (39.0)")
	void maxLatitudeAllowed() {
		Coordinate c = Coordinate.of(39.0, 126.0);
		assertNotNull(c);
	}

	@Test
	@DisplayName("경계: 최소 경도 허용 (124.0)")
	void minLongitudeAllowed() {
		Coordinate c = Coordinate.of(36.0, 124.0);
		assertNotNull(c);
	}

	@Test
	@DisplayName("경계: 최대 경도 허용 (132.0)")
	void maxLongitudeAllowed() {
		Coordinate c = Coordinate.of(36.0, 132.0);
		assertNotNull(c);
	}

	@Test
	@DisplayName("예외: 위도가 최소보다 작으면 예외")
	void latitudeBelowMinThrows() {
		assertThrows(IllegalArgumentException.class, () -> Coordinate.of(32.9, 126.0));
	}

	@Test
	@DisplayName("예외: 위도가 최대보다 크면 예외")
	void latitudeAboveMaxThrows() {
		assertThrows(IllegalArgumentException.class, () -> Coordinate.of(39.1, 126.0));
	}

	@Test
	@DisplayName("예외: 경도가 최소보다 작으면 예외")
	void longitudeBelowMinThrows() {
		assertThrows(IllegalArgumentException.class, () -> Coordinate.of(36.0, 123.9));
	}

	@Test
	@DisplayName("예외: 경도가 최대보다 크면 예외")
	void longitudeAboveMaxThrows() {
		assertThrows(IllegalArgumentException.class, () -> Coordinate.of(36.0, 132.1));
	}
}