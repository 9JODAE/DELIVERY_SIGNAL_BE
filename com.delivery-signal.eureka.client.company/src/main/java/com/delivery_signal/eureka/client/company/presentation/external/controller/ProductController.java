package com.delivery_signal.eureka.client.company.presentation.external.controller;

import com.delivery_signal.eureka.client.company.application.command.CreateProductCommand;
import com.delivery_signal.eureka.client.company.application.command.DeleteProductCommand;
import com.delivery_signal.eureka.client.company.application.command.UpdateProductCommand;
import com.delivery_signal.eureka.client.company.application.result.*;
import com.delivery_signal.eureka.client.company.application.service.ProductService;
import com.delivery_signal.eureka.client.company.presentation.external.dto.request.ProductCreateRequestDto;
import com.delivery_signal.eureka.client.company.presentation.external.dto.request.ProductUpdateRequestDto;
import com.delivery_signal.eureka.client.company.presentation.external.dto.response.*;
import com.delivery_signal.eureka.client.company.presentation.external.mapper.command.ProductCreateMapper;
import com.delivery_signal.eureka.client.company.presentation.external.mapper.command.ProductDeleteMapper;
import com.delivery_signal.eureka.client.company.presentation.external.mapper.command.ProductUpdateMapper;
import com.delivery_signal.eureka.client.company.presentation.external.mapper.response.ProductResponseMapper;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/v1/products")
@Tag(name = "Product API", description = "상품 관련 API")
@RequiredArgsConstructor
public class ProductController {

    private final ProductService productService;

    @Operation(summary = "상품 생성", description = "신규 상품을 등록합니다.")
    @PostMapping
    public ResponseEntity<ProductCreateResponseDto> createProduct(@RequestBody ProductCreateRequestDto request) {
        CreateProductCommand command = ProductCreateMapper.toCommand(request);
        ProductCreateResult result = productService.createProduct(command);
        ProductCreateResponseDto response = ProductResponseMapper.toCreateResponse(result);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @Operation(summary = "상품 단건 조회", description = "상품 상세 정보를 조회합니다.")
    @GetMapping("/{productId}")
    public ResponseEntity<ProductDetailResponseDto> getProductById(@PathVariable UUID productId) {
        ProductDetailResult result = productService.getProductById(productId);
        ProductDetailResponseDto response = ProductResponseMapper.toDetailResponse(result);
        return ResponseEntity.ok(response);
    }

    @Operation(summary = "상품 리스트 조회", description = "전체 상품 목록을 조회합니다.")
    @GetMapping
    public ResponseEntity<List<ProductListResponseDto>> getAllProducts() {
        List<ProductListResult> results = productService.getAllProducts();
        List<ProductListResponseDto> response = results.stream()
                .map(ProductResponseMapper::toListResponse)
                .collect(Collectors.toList());
        return ResponseEntity.ok(response);
    }

//    @Operation(summary = "상품 수정", description = "상품 정보를 수정합니다.")
//    @PutMapping("/{productId}")
//    public ResponseEntity<ProductUpdateResponseDto> updateProduct(
//            @PathVariable UUID productId,
//            @RequestBody ProductUpdateRequestDto request) {
//
//        UpdateProductCommand command = ProductUpdateMapper.toCommand(productId, request);
//        ProductUpdateResult result = productService.updateProduct(command);
//        ProductUpdateResponseDto response = ProductResponseMapper.toUpdateResponse(result);
//        return ResponseEntity.ok(response);
//    }
//
//    @Operation(summary = "상품 삭제", description = "상품을 삭제합니다.")
//    @DeleteMapping("/{productId}")
//    public ResponseEntity<ProductDeleteResponseDto> deleteProduct(@PathVariable UUID productId) {
//        DeleteProductCommand command = ProductDeleteMapper.toCommand(productId);
//        ProductDeleteResult result = productService.deleteProduct(command);
//        ProductDeleteResponseDto response = ProductResponseMapper.toDeleteResponse(result);
//        return ResponseEntity.ok(response);
//    }
}
