package com.delivery_signal.eureka.client.hub.domain.service;

import java.util.*;

import org.springframework.stereotype.Service;

import com.delivery_signal.eureka.client.hub.domain.entity.HubRoute;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class HubPathService {

	public List<UUID> findShortestPath(List<HubRoute> allRoutes, UUID departureHubId, UUID arrivalHubId) {
		Map<UUID, List<Edge>> graph = buildGraph(allRoutes);
		Map<UUID, Integer> transitTimes = createTransitTimeMap(departureHubId);
		Map<UUID, UUID> predecessorHubs = new HashMap<>();
		Map<UUID, UUID> predecessorRoutes = new HashMap<>();
		PriorityQueue<Node> queue = createPriorityQueue(departureHubId);
		Set<UUID> visited = new HashSet<>();

		executeDijkstra(graph, transitTimes, predecessorHubs, predecessorRoutes, visited, queue, arrivalHubId);

		if (!transitTimes.containsKey(arrivalHubId)) {
			log.warn("경로를 찾을 수 없습니다. 출발: {}, 도착: {}", departureHubId, arrivalHubId);
			return Collections.emptyList();
		}

		return reconstructRoutePath(predecessorHubs, predecessorRoutes, departureHubId, arrivalHubId);
	}

	private Map<UUID, List<Edge>> buildGraph(List<HubRoute> routes) {
		Map<UUID, List<Edge>> graph = new HashMap<>();

		for (HubRoute route : routes) {
			UUID routeId = route.getHubRouteId();
			UUID departureHubId = route.getDepartureHub().getHubId();
			UUID arrivalHubId = route.getArrivalHub().getHubId();
			int minutes = route.getTransitTime().getMinutes();

			graph.computeIfAbsent(departureHubId, key -> new ArrayList<>())
				.add(new Edge(routeId, arrivalHubId, minutes));
		}

		return graph;
	}

	private Map<UUID, Integer> createTransitTimeMap(UUID departureHubId) {
		Map<UUID, Integer> transitTimes = new HashMap<>();
		transitTimes.put(departureHubId, 0);
		return transitTimes;
	}

	private PriorityQueue<Node> createPriorityQueue(UUID departureHubId) {
		PriorityQueue<Node> queue = new PriorityQueue<>(Comparator.comparingInt(Node::time));
		queue.offer(new Node(departureHubId, 0));
		return queue;
	}

	private void executeDijkstra(
		Map<UUID, List<Edge>> graph,
		Map<UUID, Integer> transitTimes,
		Map<UUID, UUID> predecessorHubs,
		Map<UUID, UUID> predecessorRoutes,
		Set<UUID> visited,
		PriorityQueue<Node> queue,
		UUID arrivalHubId
	) {
		while (!queue.isEmpty()) {
			Node current = queue.poll();
			UUID currentHubId = current.hubId;

			if (!visited.add(currentHubId)) continue;
			if (currentHubId.equals(arrivalHubId)) break;

			relaxEdges(graph, currentHubId, transitTimes, predecessorHubs, predecessorRoutes, queue, visited);
		}
	}

	private void relaxEdges(
		Map<UUID, List<Edge>> graph,
		UUID currentHubId,
		Map<UUID, Integer> transitTimes,
		Map<UUID, UUID> predecessorHubs,
		Map<UUID, UUID> predecessorRoutes,
		PriorityQueue<Node> queue,
		Set<UUID> visited
	) {
		List<Edge> neighbors = graph.getOrDefault(currentHubId, Collections.emptyList());

		for (Edge edge : neighbors) {
			UUID nextHubId = edge.arrivalHubId;
			UUID nextRouteId = edge.routeId;

			if (visited.contains(nextHubId)) continue;

			int newTime = transitTimes.get(currentHubId) + edge.time;
			int currentTime = transitTimes.getOrDefault(nextHubId, Integer.MAX_VALUE);

			if (newTime < currentTime) {
				transitTimes.put(nextHubId, newTime);
				predecessorHubs.put(nextHubId, currentHubId);
				predecessorRoutes.put(nextHubId, nextRouteId);
				queue.offer(new Node(nextHubId, newTime));
			}
		}
	}

	private List<UUID> reconstructRoutePath(
		Map<UUID, UUID> predecessorHubs,
		Map<UUID, UUID> predecessorRoutes,
		UUID departureHubId,
		UUID arrivalHubId
	) {
		LinkedList<UUID> routePath = new LinkedList<>();
		UUID currentHub = arrivalHubId;

		while (!currentHub.equals(departureHubId)) {
			UUID routeId = predecessorRoutes.get(currentHub);

			if (routeId != null) {
				routePath.addFirst(routeId);
			}

			currentHub = predecessorHubs.get(currentHub);

			if (currentHub == null) {
				log.error("경로 재구성 중 오류 발생: 끊긴 경로 발견");
				return Collections.emptyList();
			}
		}

		return routePath;
	}

	private record Node(UUID hubId, int time) {}

	private record Edge(UUID routeId, UUID arrivalHubId, int time) {}
}