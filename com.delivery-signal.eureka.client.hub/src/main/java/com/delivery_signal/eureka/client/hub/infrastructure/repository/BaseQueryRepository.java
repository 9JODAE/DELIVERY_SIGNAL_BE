package com.delivery_signal.eureka.client.hub.infrastructure.repository;

import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.dsl.DateTimePath;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import java.time.LocalDateTime;
import java.util.Set;

public abstract class BaseQueryRepository {

    protected static final Set<Integer> ALLOWED_SIZES = Set.of(10, 30, 50);
    protected static final int DEFAULT_PAGE = 0;
    protected static final int DEFAULT_SIZE = 10;
    protected static final String DEFAULT_SORT_BY = "createdAt";
    protected static final Sort.Direction DEFAULT_DIRECTION = Sort.Direction.DESC;

    protected Pageable createPageable(Integer page, Integer size) {
        int normalizedPage = normalizePage(page);
        int normalizedSize = normalizeSize(size);
        return PageRequest.of(normalizedPage, normalizedSize);
    }

    protected int normalizePage(Integer page) {
        return (page == null || page < 0) ? DEFAULT_PAGE : page;
    }

    protected int normalizeSize(Integer size) {
        return (size == null || !ALLOWED_SIZES.contains(size)) ? DEFAULT_SIZE : size;
    }

    protected String normalizeSortBy(String sortBy) {
        return sortBy == null || sortBy.isBlank() || (!"createdAt".equals(sortBy) && !"updatedAt".equals(sortBy))
            ? DEFAULT_SORT_BY
            : sortBy;
    }

    protected Sort.Direction normalizeDirection(String direction) {
        if (direction == null || direction.isBlank()) {
            return DEFAULT_DIRECTION;
        }
        return "ASC".equalsIgnoreCase(direction) ? Sort.Direction.ASC : Sort.Direction.DESC;
    }

    protected OrderSpecifier<?> buildOrderSpecifier(
        String sortBy,
        String direction,
        DateTimePath<LocalDateTime> createdAt,
        DateTimePath<LocalDateTime> updatedAt
    ) {
        String normalizedSortBy = normalizeSortBy(sortBy);
        Sort.Direction normalizedDirection = normalizeDirection(direction);

        return isCreatedAtSort(normalizedSortBy)
            ? buildOrder(createdAt, normalizedDirection)
            : buildOrder(updatedAt, normalizedDirection);
    }

	private boolean isCreatedAtSort(String sortBy) {
		return "createdAt".equals(sortBy);
	}

    private OrderSpecifier<?> buildOrder(DateTimePath<LocalDateTime> path, Sort.Direction direction) {
        return direction.isAscending() ? path.asc() : path.desc();
    }
}