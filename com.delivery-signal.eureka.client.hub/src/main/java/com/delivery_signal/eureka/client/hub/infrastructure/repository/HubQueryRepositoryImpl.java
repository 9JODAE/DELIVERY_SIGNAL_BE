package com.delivery_signal.eureka.client.hub.infrastructure.repository;

import com.delivery_signal.eureka.client.hub.domain.model.Hub;
import com.delivery_signal.eureka.client.hub.domain.model.QHub;
import com.delivery_signal.eureka.client.hub.domain.repository.HubQueryRepository;
import com.delivery_signal.eureka.client.hub.domain.vo.HubSearchCondition;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Repository;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.Set;

@Repository
@RequiredArgsConstructor
public class HubQueryRepositoryImpl implements HubQueryRepository {

    private static final QHub HUB = QHub.hub;
	private static final Set<Integer> ALLOWED_SIZES = Set.of(10, 30, 50);
	private static final int DEFAULT_PAGE = 0;
	private static final int DEFAULT_SIZE = 10;
	private static final String DEFAULT_SORT_BY = "createdAt";
	private static final Sort.Direction DEFAULT_DIRECTION = Sort.Direction.DESC;

    private final JPAQueryFactory queryFactory;

    @Override
    public Page<Hub> searchHubs(HubSearchCondition condition) {
        Pageable pageable = createPageable(condition);
        BooleanBuilder conditions = buildSearchConditions(condition);
        OrderSpecifier<?> orderSpecifier = buildOrderSpecifier(condition);

        List<Hub> hubs = fetchHubs(conditions, orderSpecifier, pageable);
        long totalCount = countHubs(conditions);

        return new PageImpl<>(hubs, pageable, totalCount);
    }

    private Pageable createPageable(HubSearchCondition condition) {
		int page = normalizePage(condition.page());
		int size = normalizeSize(condition.size());
		return PageRequest.of(page, size);
    }

	private int normalizePage(Integer page) {
		return (page == null || page < 0) ? DEFAULT_PAGE : page;
	}

	private int normalizeSize(Integer size) {
		return (size == null || !ALLOWED_SIZES.contains(size)) ? DEFAULT_SIZE : size;
	}

    private List<Hub> fetchHubs(BooleanBuilder conditions, OrderSpecifier<?> order, Pageable pageable) {
        return queryFactory
                .selectFrom(HUB)
                .where(conditions)
                .orderBy(order)
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();
    }

    private long countHubs(BooleanBuilder conditions) {
        Long count = queryFactory
                .select(HUB.count())
                .from(HUB)
                .where(conditions)
                .fetchOne();

        return count != null ? count : 0L;
    }

    private BooleanBuilder buildSearchConditions(HubSearchCondition condition) {
        BooleanBuilder builder = new BooleanBuilder();

		addNameCondition(builder, condition.name());
		addAddressCondition(builder, condition.address());

        return builder;
    }

    private void addNameCondition(BooleanBuilder builder, String name) {
        if (StringUtils.hasText(name)) {
            builder.and(HUB.name.contains(name));
        }
    }

    private void addAddressCondition(BooleanBuilder builder, String address) {
        if (StringUtils.hasText(address)) {
            builder.and(HUB.address.value.contains(address));
        }
    }

	private OrderSpecifier<?> buildOrderSpecifier(HubSearchCondition condition) {
		String sortBy = normalizeSortBy(condition.sortBy());
		Sort.Direction direction = normalizeDirection(condition.direction());

		return isCreatedAtSort(sortBy)
			? buildCreatedAtOrder(direction)
			: buildUpdatedAtOrder(direction);
	}

	private String normalizeSortBy(String sortBy) {
		return sortBy == null || sortBy.isBlank() || (!"createdAt".equals(sortBy) && !"updatedAt".equals(sortBy))
			? DEFAULT_SORT_BY
			: sortBy;
	}

	private Sort.Direction normalizeDirection(String direction) {
		if (direction == null || direction.isBlank()) {
			return DEFAULT_DIRECTION;
		}
		return "ASC".equalsIgnoreCase(direction) ? Sort.Direction.ASC : Sort.Direction.DESC;
	}

    private boolean isCreatedAtSort(String sortBy) {
        return "createdAt".equals(sortBy);
    }

	private OrderSpecifier<?> buildCreatedAtOrder(Sort.Direction direction) {
		return direction.isAscending() ? HUB.createdAt.asc() : HUB.createdAt.desc();
	}

	private OrderSpecifier<?> buildUpdatedAtOrder(Sort.Direction direction) {
		return direction.isAscending() ? HUB.updatedAt.asc() : HUB.updatedAt.desc();
	}
}