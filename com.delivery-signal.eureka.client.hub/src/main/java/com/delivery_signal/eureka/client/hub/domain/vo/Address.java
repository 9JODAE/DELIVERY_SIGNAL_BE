package com.delivery_signal.eureka.client.hub.domain.vo;

import jakarta.persistence.Embeddable;
import lombok.AccessLevel;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Embeddable
@EqualsAndHashCode
@NoArgsConstructor(access = AccessLevel.PROTECTED, force = true)
public class Address {

	private static final int MIN_ADDRESS_LENGTH = 12;
	private static final int MAX_ADDRESS_LENGTH = 100;

	private String value;

	private Address(String address) {
		validateAddress(address);
		this.value = address;
	}

	public static Address of(String address) {
		return new Address(address);
	}

	private void validateAddress(String address) {
		if (address == null || address.isBlank()) {
			throw new IllegalArgumentException("주소는 필수입니다.");
		}
		if (address.length() < MIN_ADDRESS_LENGTH || address.length() > MAX_ADDRESS_LENGTH) {
			throw new IllegalArgumentException(
				"주소는 " + MIN_ADDRESS_LENGTH + "자 이상 " + MAX_ADDRESS_LENGTH + "자 이하여야 합니다.");
		}
	}
}
