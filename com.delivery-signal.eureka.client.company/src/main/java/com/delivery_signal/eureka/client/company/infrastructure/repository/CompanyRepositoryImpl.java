package com.delivery_signal.eureka.client.company.infrastructure.repository;

import com.delivery_signal.eureka.client.company.domain.entity.Company;
import com.delivery_signal.eureka.client.company.domain.entity.QCompany;
import com.delivery_signal.eureka.client.company.domain.repository.CompanyRepository;
import com.delivery_signal.eureka.client.company.domain.repository.CompanyRepositoryCustom;
import com.querydsl.jpa.impl.JPAQueryFactory;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

import static com.delivery_signal.eureka.client.company.domain.entity.QCompany.company;

@Repository
public class CompanyRepositoryImpl implements CompanyRepositoryCustom {

    private final JPAQueryFactory queryFactory;

    public CompanyRepositoryImpl(JPAQueryFactory queryFactory) {
        this.queryFactory = queryFactory;
    }

    @Override
    public List<Company> findAllByDeletedAtIsNull() {
        return queryFactory
                .selectFrom(company)
                .where(company.deletedAt.isNull())
                .fetch();
    }

    @Override
    public List<Company> findByHubIdAndDeletedAtIsNull(UUID hubId) {
        return queryFactory
                .selectFrom(company)
                .where(company.hubId.eq(hubId)
                        .and(company.deletedAt.isNull()))
                .fetch();
    }
}
