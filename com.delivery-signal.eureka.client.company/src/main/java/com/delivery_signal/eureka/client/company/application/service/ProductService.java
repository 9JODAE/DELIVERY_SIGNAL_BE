package com.delivery_signal.eureka.client.company.application.service;


import com.delivery_signal.eureka.client.company.application.command.CreateProductCommand;
import com.delivery_signal.eureka.client.company.application.command.DeleteProductCommand;
import com.delivery_signal.eureka.client.company.application.command.UpdateProductCommand;
import com.delivery_signal.eureka.client.company.application.mapper.ProductQueryMapper;
import com.delivery_signal.eureka.client.company.application.port.out.CompanyQueryPort;
import com.delivery_signal.eureka.client.company.application.result.*;
import com.delivery_signal.eureka.client.company.application.validator.ProductPermissionValidator;
import com.delivery_signal.eureka.client.company.common.NotFoundException;
import com.delivery_signal.eureka.client.company.domain.entity.Company;
import com.delivery_signal.eureka.client.company.domain.entity.Product;
import com.delivery_signal.eureka.client.company.domain.repository.ProductRepository;
import com.delivery_signal.eureka.client.company.domain.service.ProductDomainService;
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

    private final CompanyService companyService;

    // 도메인 / 검증 / 매퍼
    private final ProductDomainService productDomainService;
    private final ProductPermissionValidator productPermissionValidator;
    private final ProductQueryMapper productQueryMapper;


    /**
     * 상품 등록
     */
    public ProductCreateResult createProduct(CreateProductCommand command) {

        productPermissionValidator.validateCreate(command.getUserId(),command.getCompanyId(),command.getHubId());

        CompanyDetailResult companyInfo = companyService.getCompanyById(command.getCompanyId());
        if (companyInfo == null) {
            throw new NotFoundException("업체", command.getCompanyId());
        }

        Product product = productDomainService.createProduct(
                command.getProductName(),
                command.getPrice(),
                command.getCompanyId(),
                command.getHubId(),
                command.getUserId()
        );

        productRepository.save(product);

        log.info("상품 생성 완료: {}", product.getProductId());

        return ProductCreateResult.builder()
                .productId(UUID.randomUUID())
                .productName(product.getProductName())
                .price(product.getPrice())
                .companyId(product.getCompanyId())
                .hubId(product.getHubId())
                .createdAt(product.getCreatedAt())
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


    /**
     * 상품 수정
     */
    public ProductUpdateResult updateProduct(UpdateProductCommand command) {

        Product product = productRepository.findById(command.getProductId())
                .orElseThrow(() -> new NotFoundException("",command.getProductId()));

        // 권한 검증
        productPermissionValidator.validateUpdate(command.getUserId(),product.getCompanyId(),product.getHubId());

        // 소속 업체 검증
        CompanyDetailResult companyresult = companyService.getCompanyById(product.getCompanyId());
        if (companyresult == null) {
            throw new NotFoundException("업체", product.getCompanyId());
        }

        // 도메인 로직 호출
        product.updateInfo(command.getProductName(), command.getPrice(), command.getUserId());

        productRepository.save(product);

        return ProductUpdateResult.builder()
                .productId(product.getProductId())
                .price(product.getPrice())
                .productName(product.getProductName())
                .updatedBy(product.getUpdatedBy())
                .updateAt(product.getUpdatedAt())
                .message("상품 정보가 수정되었습니다.")
                .build();
    }


    /**
     * 상품 삭제
     */
    public ProductDeleteResult deleteProduct(DeleteProductCommand command) {

        Product product = productRepository.findById(command.getProductId())
                .orElseThrow(() -> new NotFoundException("",command.getProductId()));

        // 권한 검증
        productPermissionValidator.validateDelete(command.getUserId(),product.getCompanyId());

        // 삭제 처리 (soft delete)
        product.softDelete(command.getUserId());
        product.setDeletedBy(command.getUserId());

        log.info("상품 삭제 완료: {}", product.getProductName());

        return ProductDeleteResult.builder()
                .productId(product.getProductId())
                .deletedBy(product.getDeletedBy())
                .deletedAt(product.getDeletedAt())
                .message("상품이 삭제되었습니다.")
                .build();
    }
}
