package com.delivery_signal.eureka.client.company.infrastructure.repository;

import com.delivery_signal.eureka.client.company.domain.entity.Product;
import com.delivery_signal.eureka.client.company.domain.entity.QProduct;
import com.delivery_signal.eureka.client.company.domain.repository.ProductRepositoryCustom;
import com.querydsl.jpa.impl.JPAQueryFactory;

import java.util.List;
import java.util.UUID;

import static com.delivery_signal.eureka.client.company.domain.entity.QProduct.product;

public class ProductRepositoryImpl implements ProductRepositoryCustom {

    private final JPAQueryFactory queryFactory;

    public ProductRepositoryImpl(JPAQueryFactory queryFactory) {
        this.queryFactory = queryFactory;
    }

    @Override
    public List<Product> findAllByDeletedAtIsNull() {
        return queryFactory
                .selectFrom(product)
                .where(product.deletedAt.isNull())
                .fetch();
    }

    @Override
    public List<Product> findByHubIdAndDeletedAtIsNull(UUID hubId) {
        return queryFactory
                .selectFrom(product)
                .where(product.hubId.eq(hubId)
                        .and(product.deletedAt.isNull()))
                .fetch();
    }
}
