package com.delivery_signal.eureka.client.user.domain.entity;
import com.delivery_signal.eureka.client.user.domain.common.entity.BaseEntity;
import jakarta.persistence.*;

import lombok.*;

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
    private String organization;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private ApprovalStatus approvalStatus = ApprovalStatus.PENDING; // PENDING / APPROVED/ REJECTED

    @Column(nullable = false)
    private Boolean isPublic = false;


    @Builder
    public User(String username, String password, String slackId, String organization, UserRole role) {
        this.username = username;
        this.password = password;
        this.slackId = slackId;
        this.organization = organization;
        this.role = role;
        this.approvalStatus = ApprovalStatus.PENDING;
        this.isPublic = false;
    }

    // 수정 메서드 추가
    public void updateIsPublic(Boolean isPublic) {
        this.isPublic = isPublic;
    }
    public void updateRole(UserRole role) {
        this.role = role;
    }

}



