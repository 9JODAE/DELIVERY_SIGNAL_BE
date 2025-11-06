package com.delivery_signal.eureka.client.hub.application.command;

/**
 * 허브 검색 Command
 */
public record SearchHubCommand(
	String name,
	String address,
	Integer page,
	Integer size,
	String sortBy,
	String direction
) {
	public static SearchHubCommand of(
		String name,
		String address,
		Integer page,
		Integer size,
		String sortBy,
		String direction
	) {
		return new SearchHubCommand(name, address, page, size, sortBy, direction);
	}
}
