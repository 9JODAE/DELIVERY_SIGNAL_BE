package com.delivery_signal.eureka.client.user.domain.model;
import jakarta.persistence.*;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.util.UUID;

@Getter
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "p_users")
@Entity
public class User extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(nullable = false)
    private Long userId;

    @Column(nullable = false, length = 100)
    private String username;

    @Column(nullable = false, unique = true)
    private String slackId;

    @Column(nullable = false)
    private String password;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private UserRole role;  // MASTER / HUB_MANAGER / DELIVERY_MANAGER / SUPPLIER_MANAGER

    @Column(nullable=false)
    private String organization;  // hubId, companyId, deliveryId (UserRole과 비교 필요, MASTER는 organization 없음)

    @Column(nullable = true)
    private UUID organizationId;  // hubId, companyId, deliveryId (UserRole과 비교 필요, MASTER는 organizationId 없음)

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private ApprovalStatus approvalStatus = ApprovalStatus.PENDING; // PENDING / APPROVED/ REJECTED

    @Column(nullable = false)
    private Boolean isPublic = false;


    @Builder
    public User(String username, String password, String slackId, String organization, UUID organizationId, UserRole role) {
        this.username = username;
        this.password = password;
        this.slackId = slackId;
        this.organization = organization;
        this.organizationId = organizationId;
        this.role = role;
        this.approvalStatus = ApprovalStatus.PENDING;
        this.isPublic = false;
    }

    // 수정 메서드 추가

    public void updateUsername(@Size(min = 3, max = 10, message = "이름은 3~10자로 입력해야 합니다") @Pattern(
            regexp = "^[a-z0-9]{3,10}$",
            message = "이름은 알파벳 소문자(a~z)와 숫자(0~9)로만 구성되어야 합니다"
    ) String username) {
        this.username = username;
    }

    public void updatePassword(@Size(min = 8, max = 15, message = "비밀번호는 8~15자로 입력해야 합니다") @Pattern(
            regexp = "^(?=.*[a-zA-Z])(?=.*\\d)(?=.*[!@#$%^&*()_+=\\-{}\\[\\]:;\"'<>,.?/]).*$",
            message = "비밀번호는 알파벳 대소문자와 숫자, 특수문자를 포함해야 합니다"
    ) String password) {
        this.password = password;
    }

    public void updateSlackId(String s) {
        this.slackId = s;
    }

    public void updateOrganization(String organization) {
        this.organization = organization;
    }

//    public void updateRole(UserRole role) { this.role = role; }

    public void updateApprovalStatus(ApprovalStatus approvalStatus) {
        this.approvalStatus = approvalStatus;
    }

    public void updateIsPublic(Boolean isPublic) {
        this.isPublic = isPublic;
    }
}



