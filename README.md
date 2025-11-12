# 🚚 DELIVERY_SIGNAL Logistics

> MSA 기반 국내 B2B 물류 관리 및 배송 시스템



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

##  팀원 소개

| 이름      | 역할 | 주요 담당 |
|---------|----|-------|
| **김지혜** | 업체 | 업체,상품 |
| **민송경** | 배송 | 배송,경로 |
| **고민정** | 유저 | 유저,인증 |
| **진주양** | 주문 | 주문    |
| **양지웅** | 허브 | 허브    | 
| **박용재** | 외부 | 슬랙,Ai |

---
## 아키텍쳐
<img width="892" height="567" alt="Image" src="https://teamsparta.notion.site/image/attachment%3Aba4c633a-1b57-4c1e-8a50-705902a8e154%3Aimage.png?table=block&id=2a02dc3e-f514-808e-a4bc-ef8e6650a4ad&spaceId=83c75a39-3aba-4ba4-a792-7aefe4b07895&width=2000&userId=&cache=v2" />

### 🔗 ERD
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
- 허브 등록 / 조회 / 수정 / 삭제 처리

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
