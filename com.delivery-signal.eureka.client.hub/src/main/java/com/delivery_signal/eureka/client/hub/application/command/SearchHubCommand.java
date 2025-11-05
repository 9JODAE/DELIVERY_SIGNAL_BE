package com.delivery_signal.eureka.client.hub.application.command;

import org.springframework.data.domain.Sort;

public record SearchHubCommand(
	String name,
	String address,
	Integer page,
	Integer size,
	String sortBy,
	Sort.Direction direction
) {
	public static SearchHubCommand of(
		String name,
		String address,
		Integer page,
		Integer size,
		String sortBy,
		Sort.Direction direction
	) {
		return new SearchHubCommand(name, address, page, size, sortBy, direction);
	}
}
