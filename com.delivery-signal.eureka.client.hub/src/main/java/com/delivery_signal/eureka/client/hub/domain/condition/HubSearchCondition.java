package com.delivery_signal.eureka.client.hub.domain.condition;

public record HubSearchCondition(
	String name,
	String address,
	Integer page,
	Integer size,
	String sortBy,
	String direction
) {
	public static HubSearchCondition of(
		String name,
		String address,
		Integer page,
		Integer size,
		String sortBy,
		String direction
	) {
		return new HubSearchCondition(name, address, page, size, sortBy, direction);
	}
}
