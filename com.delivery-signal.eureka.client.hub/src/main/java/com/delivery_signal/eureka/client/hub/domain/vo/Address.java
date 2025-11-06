package com.delivery_signal.eureka.client.hub.domain.vo;

import jakarta.persistence.Embeddable;
import lombok.AccessLevel;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Embeddable
@EqualsAndHashCode
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Address {

	private String value;

	public Address(String address) {
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
		if (address.length() < 12 || address.length() > 100) {
			throw new IllegalArgumentException("주소는 12자 이상 100자 이하여야 합니다.");
		}
	}
}
