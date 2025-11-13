# 🚚 DELIVERY_SIGNAL Logistics

> MSA 기반 국내 B2B 물류 관리 및 배송 시스템

---

💡 프로젝트 배경</br>
국내 물류 산업은 다양한 지역 허브를 중심으로 복잡한 공급망 구조를 가지고 있습니다. </br>
특히, 기업 간 거래(B2B)에서는 대량 물류, 다수의 허브 운영, 재고 이동, 권한 기반 관리가 필수적이며 복잡한 프로세스를 요구합니다.</br>
기존 시스템은 이러한 복잡성을 수동으로 관리하여 효율이 떨어졌습니다.</br>
</br>
🧭 프로젝트 개요</br>
국내 B2B(Business to Business) 물류 운영을 효율적으로 관리하기 위한 MSA 기반 물류 및 배송 시스템입니다.</br>
기업 간 거래 환경에서 발생하는 허브 간 재고 이동, 주문 처리, 배송 관리, 사용자 권한 제어 등을 통합적으로 관리할 수 있도록 설계되었습니다.</br>

Delivery Signal 물류 시스템은</br>
“허브 간 이동부터 최종 배송까지의 모든 물류 흐름을 자동화하고 추적할 수 있는 시스템”</br>
을 목표로 합니다.</br>
</br>
✅ 물류 운영 효율화 – 허브 간 재고 이동, 배송 경로 자동화</br>
✅ 업무 투명성 강화 – Slack 메시지 알림으로 주문부터 배송까지 전 단계 추적 가능</br>
✅ 확장성 높은 구조 – MSA 기반의 독립 배포 및 유지보수 용이</br>
✅ 보안 강화 – JWT 기반 인증과 역할별 접근 제어로 안정성 확보</br>

--- 

### 🛠️ 개발 환경
![Java](https://img.shields.io/badge/Java-17-007396?logo=java&logoColor=white)
![Spring Boot](https://img.shields.io/badge/Spring%20Boot-3.5.6-6DB33F?logo=springboot&logoColor=white)
![Spring Security](https://img.shields.io/badge/Spring%20Security-6DB33F?logo=springsecurity&logoColor=white)
![Spring Data JPA](https://img.shields.io/badge/Spring%20Data%20JPA-6DB33F?logo=spring&logoColor=white)
![Gradle](https://img.shields.io/badge/Gradle-02303A?logo=gradle&logoColor=white)
![Spring cloud](https://img.shields.io/badge/MSA_Architecture-Spring_Cloud-6DB33F?logo=spring&logoColor=white)
![PostgreSQL](https://img.shields.io/badge/PostgreSQL-4169E1?logo=postgresql&logoColor=white)

![Slack DM](https://img.shields.io/badge/Slack-DM-4A154B?logo=slack&logoColor=white)
![Google AI](https://img.shields.io/badge/Google%20AI-genai--1.8.0-4285F4?logo=google&logoColor=white)

![Swagger](https://img.shields.io/badge/Swagger-85EA2D?logo=swagger&logoColor=black)

![Git](https://img.shields.io/badge/Git-F05032?logo=git&logoColor=white)
![GitHub](https://img.shields.io/badge/GitHub-181717?logo=github&logoColor=white)

![IntelliJ IDEA](https://img.shields.io/badge/IntelliJ%20IDEA-000000?logo=intellijidea&logoColor=white)

---

### 🛠️ 사용 기술 스택

| **구분** | **기술** |
|-----------|-----------|
| **Architecture** | Spring Cloud (Eureka, Gateway, Feign Client) |
| **Monitoring / 운영 관리** | Spring Boot Actuator, Resilience4J (Circuit Breaker) |
| **Security** | Spring Security / JWT |
| **AOP / 공통처리** | Spring AOP (로깅, 권한 검사, 트랜잭션 관리 등) |
| **Persistence Layer** | JPA / QueryDSL |
| **Cache / 동시성 제어** | Redis / Redisson |
| **API 문서화** | Swagger (Springdoc OpenAPI 3) |
| **외부 연동** | Gemini API, Slack API 등 External API |
| **통신** | Spring Cloud OpenFeign (MSA 간 REST 통신) |


---

##  팀원 소개

| 이름      | 역할 | 주요 담당 |
|---------|----|-------|
| **김지혜** | 업체 | 업체,상품 |
| **민송경** | 배송 | 배송,경로 |
| **고민정** | 유저 | 유저,인증 |
| **진주양** | 주문 | 주문    |
| **양지웅** | 허브 | 허브, 게이트웨이 | 
| **박용재** | 외부 | 슬랙, AI |

---
## 아키텍쳐
MSA 기반의 서비스 간 데이터 연동 및 확장성을 확보하고, 인증, 권한, 트랜잭션 등 
엔터프라이즈급 백엔드 기술 통합으로 </br> “운영 중심의 물류 관리 시스템” 설계를 완성하였습니다
<img width="892" height="567" alt="Image" src="https://teamsparta.notion.site/image/attachment%3Aba4c633a-1b57-4c1e-8a50-705902a8e154%3Aimage.png?table=block&id=2a02dc3e-f514-808e-a4bc-ef8e6650a4ad&spaceId=83c75a39-3aba-4ba4-a792-7aefe4b07895&width=2000&userId=&cache=v2" />

### 🔗 ERD
도메인 주도 설계(DDD) 방식을 적용위한 바운디드 컨텍스트 구분
<img width="892" height="567" alt="Image" src="https://teamsparta.notion.site/image/attachment%3A85e49242-5052-4349-9b28-11fbaab0e067%3Aimage.png?table=block&id=2a02dc3e-f514-805c-84b6-d1cd27bf4903&spaceId=83c75a39-3aba-4ba4-a792-7aefe4b07895&width=2000&userId=&cache=v2" />

---
## 서비스 실행 방법

###   환경 설정

```java

```

##  주요 기능

###  회원 (User)
- 회원 등록 / 조회 / 수정 / 삭제 처리
- 회원가입 / 로그인 (JWT)

###  업체 (Company)
- 업체 등록 / 조회 / 수정 / 삭제 처리

###  상품 (Product)
- 상품 등록 / 조회 / 수정 / 삭제 처리

### 주문 (Order)
- 주문 등록 / 조회 / 수정 / 삭제 처리

###  허브 (Hub)
- 허브 등록 / 검색 / 조회 / 수정 / 삭제 처리
- 재고 등록 / 검색 / 조회 / 수정 / 삭제 / 차감 / 복구 처리
  - Redisson Lock 적용
- 이동 정보 등록 / 검색 / 조회 / 수정 / 삭제 처리
  - Redis를 사용하여 캐싱 적용, 경로 알고리즘 Dijkstra 적용

###  외부서비스 (External)
- Google ai geminai 연동
  - 주문정보를 바탕으로 최종발송시한 계산
  
- Slack 연동
  - MessageBot 추가
  - MessageBot 으로 최종 발송 시한 배송담당자에게 DM 전송
  - 전송된 DM을 SlackRecord로 기록관리

---

### 🔗 디렉토리 구조
<details>
    <summary><strong>디렉토리 구조</strong></summary>

```
com.example.myapp
├── application
│   ├── service
│   │   ├── OrderService.java
│   │   ├── UserService.java
│   │   └── OrderMessageService.java
│   ├── dto
│   │   └── OrderDTO.java
├── domain
│   ├── model
│   │   ├── Order.java
│   │   ├── Product.java
│   │   └── ValueObject.java
│   ├── repository
│   │   └── OrderRepository.java
│   └── service
│       └── OrderDomainService.java
├── infrastructure
│   ├── repository
│   │   ├── JpaOrderRepository.java
│   │   ├── OrderRepositoryImpl.java
│   │   └── OrderQueryDSLRepositoryImpl.java
│   ├── client
│   │   └── UserClient.java
│   ├── configuration
│   │   └── DatabaseConfig.java
│   └── messaging
│       ├── OrderMessageConsumer.java
│       └── OrderMessageProducer.java
│
└── presentation
    ├── controller
    │   └── OrderController.java
    └── request
        └── OrderRequest.java
```

</details>


### 📄 API 명세서

[API Notion Link](https://teamsparta.notion.site/9-29d2dc3ef51480f9aa27e4fe4792d219)
