package com.delivery_signal.eureka.client.company.application.service;


import com.delivery_signal.eureka.client.company.application.command.*;
import com.delivery_signal.eureka.client.company.application.mapper.ProductQueryMapper;
import com.delivery_signal.eureka.client.company.application.port.out.CompanyQueryPort;
import com.delivery_signal.eureka.client.company.application.port.out.UserQueryPort;
import com.delivery_signal.eureka.client.company.application.result.*;
import com.delivery_signal.eureka.client.company.application.validator.ProductPermissionValidator;
import com.delivery_signal.eureka.client.company.common.NotFoundException;
import com.delivery_signal.eureka.client.company.domain.entity.Product;
import com.delivery_signal.eureka.client.company.domain.repository.ProductRepository;
import com.delivery_signal.eureka.client.company.domain.service.ProductDomainService;
import com.delivery_signal.eureka.client.company.domain.vo.company.CompanyInfo;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class ProductService {

    private final ProductRepository productRepository;

    // 외부 MSA 포트
    private final CompanyQueryPort companyQueryPort;
    private final UserQueryPort userQueryPort;

    // 도메인 / 검증 / 매퍼
    private final ProductDomainService productDomainService;
    private final ProductPermissionValidator productPermissionValidator;
    private final ProductQueryMapper productQueryMapper;


    /**
     * 상품 등록
     */
    public ProductCreateResult createProduct(CreateProductCommand command) {

        // 1️⃣ 사용자 권한 체크
        productPermissionValidator.validateCreate(command.getUserId(),command.getCompanyId(),command.getHubId());

        // 2️⃣ 유저 승인 여부 확인
        if (!userQueryPort.isUserApproved(command.getUserId())) {
            throw new NotFoundException("",command.getUserId());
        }

        // 3️⃣ 소속 업체 존재 여부 확인
        CompanyDetailResult companyInfo = companyQueryPort.getCompanyById(command.getCompanyId());
        if (companyInfo == null) {
            throw new NotFoundException("업체", command.getCompanyId());
        }

        // 4️⃣ 도메인 엔티티 생성
        Product product = productDomainService.createProduct(
                command.getCompanyId(),
                command.getName(),
                command.getPrice(),
                command.getUserId()
        );

        // 5️⃣ 저장
        productRepository.save(product);

        log.info("상품 생성 완료: {}", product.getId());

        return ProductCreateResult.builder()
                .id(product.getId())
                .name(product.getName())
                .hubId(product.getHubId())
                .companyId(product.getCompanyId())
                .build();
    }


    /**
     * 상품 단건 조회
     */
    @Transactional(readOnly = true)
    public ProductDetailResult getProductById(UUID productId) {
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new NotFoundException("", productId));
        return productQueryMapper.toDetailDto(product);
    }


    /**
     * 전체 상품 조회
     */
    @Transactional(readOnly = true)
    public List<ProductListResult> getAllProducts() {
        List<Product> products = productRepository.findAll();
        return productQueryMapper.toListDtos(products);
    }


//    /**
//     * 상품 수정
//     */
//    public ProductUpdateResult updateProduct(UpdateProductCommand command) {
//
//        Product product = productRepository.findById(command.getProductId())
//                .orElseThrow(() -> new NotFoundException("",command.getProductId()));
//
//        // 권한 검증
//        productPermissionValidator.validateUpdate(command.getUserId(),product.getCompanyId(),product.getHubId());
//
//        // 소속 업체 검증
//        CompanyInfo companyInfo = companyQueryPort.getCompanyById());
//        if (companyInfo == null) {
//            throw new NotFoundException("업체", product.getCompanyId());
//        }
//
//        // 도메인 로직 호출
//        product.updateInfo(command.getProductName(), command.getPrice(), command.getDescription());
//
//        productRepository.save(product);
//
//        return ProductUpdateResult.builder()
//                .productId(com.delivery_signal.eureka.client.company.domain.entity.product.getProductId())
//                .updatedBy(product.getUpdatedBy())
//                .updatedAt(product.getUpdatedAt())
//                .message("상품 정보가 수정되었습니다.")
//                .build();
//    }
//
//
//    /**
//     * 상품 삭제
//     */
//    public ProductDeleteResult deleteProduct(DeleteProductCommand command) {
//
//        Product product = productRepository.findByProductId(command.getProductId())
//                .orElseThrow(() -> new ProductNotFoundException(command.getProductId()));
//
//        // 권한 검증
//        productPermissionValidator.validateDelete(command.getUserId(), com.delivery_signal.eureka.client.company.domain.entity.product.getCreatedBy());
//
//        // 삭제 처리 (soft delete)
//        product.markAsDeleted(LocalDateTime.now());
//        productRepository.save(product);
//
//        log.info("상품 삭제 완료: {}", product.getProductName());
//
//        return ProductDeleteResult.builder()
//                .productId(com.delivery_signal.eureka.client.company.domain.entity.product.getProductId())
//                .deletedBy(product.getDeletedBy())
//                .deletedAt(product.getDeletedAt())
//                .message("상품이 삭제되었습니다.")
//                .build();
//    }
}
