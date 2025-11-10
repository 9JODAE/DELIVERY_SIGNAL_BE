package com.delivery_signal.eureka.client.hub.domain.vo;

import java.util.UUID;

import jakarta.persistence.Access;
import jakarta.persistence.AccessType;
import jakarta.persistence.Embeddable;
import lombok.AccessLevel;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Embeddable
@EqualsAndHashCode
@Access(AccessType.FIELD)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ProductId {

	private UUID value;

	public ProductId(UUID productId) {
		if (productId == null) {
			throw new IllegalArgumentException("유효하지 않은 상품 ID입니다");
		}
		this.value = productId;
	}

	public static  ProductId of(UUID productId) {
		return new ProductId(productId);
	}
}
