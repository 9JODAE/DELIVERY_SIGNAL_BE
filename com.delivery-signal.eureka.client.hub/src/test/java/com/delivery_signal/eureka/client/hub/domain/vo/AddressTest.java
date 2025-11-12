package com.delivery_signal.eureka.client.hub.domain.vo;

import static org.assertj.core.api.AssertionsForClassTypes.*;
import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class AddressTest {

	@Test
	@DisplayName("정상: 유효한 주소 생성")
	void createWithFactoryMethod() {
		String addr = "경기도 성남시 분당구 불정로 6";
		Address address = Address.of(addr);
		assertNotNull(address);
	}

	@Test
	@DisplayName("정상: 동일한 주소 값 객체는 동일")
	void equalityOfSameAddress() {
		String addr = "부산광역시 해운대구 우동 1234";
		Address address1 = Address.of(addr);
		Address address2 = Address.of(addr);

		assertThat(address1).isEqualTo(address2);
		assertEquals(address1.hashCode(), address2.hashCode());
	}

	@Test
	@DisplayName("예외: null 입력 시 IllegalArgumentException")
	void nullAddressThrows() {
		assertThrows(IllegalArgumentException.class, () -> Address.of(null));
	}

	@Test
	@DisplayName("예외: 공백 문자열 입력 시 IllegalArgumentException")
	void blankAddressThrows() {
		assertThrows(IllegalArgumentException.class, () -> Address.of("   "));
	}

	@Test
	@DisplayName("경계: 최소 길이(12)인 주소는 허용")
	void minLengthAllowed() {
		String addr = "가".repeat(12);
		Address address = Address.of(addr);
		assertNotNull(address);
	}

	@Test
	@DisplayName("경계: 최대 길이(100)인 주소는 허용")
	void maxLengthAllowed() {
		String addr = "가".repeat(100);
		Address address = Address.of(addr);
		assertNotNull(address);
	}

	@Test
	@DisplayName("경계: 최소 길이 - 1 (11) 인 경우 예외")
	void belowMinLengthThrows() {
		String addr = "가".repeat(11);
		assertThrows(IllegalArgumentException.class, () -> Address.of(addr));
	}

	@Test
	@DisplayName("경계: 최대 길이 + 1 (101) 인 경우 예외")
	void aboveMaxLengthThrows() {
		String addr = "가".repeat(101);
		assertThrows(IllegalArgumentException.class, () -> Address.of(addr));
	}
}