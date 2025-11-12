package com.delivery_signal.eureka.client.company.domain.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "p_companies")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Company extends BaseEntity{

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID companyId;

    @Column(name = "hub_id", nullable = false)
    private UUID hubId;

    @Column(name = "company_name", nullable = false)
    private String companyName;

    @Enumerated(EnumType.STRING)
    @Column(name = "company_type", nullable = false)
    private CompanyType companyType;

    @Column(name = "company_address")
    private String address;

    public void updateInfo(String companyName, String address, CompanyType type, Long userId) {
        this.companyName = companyName;
        this.address = address;
        this.companyType = type;
        this.setUpdatedBy(userId);
    }

    public void softDelete(Long userId){
        if (this.isDeleted()) {
            return;
        }
        this.delete(userId);
    }
}
