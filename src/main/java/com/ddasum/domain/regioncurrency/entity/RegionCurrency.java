package com.ddasum.domain.regioncurrency.entity;

import com.ddasum.domain.user.entity.User;
import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "TBL_REGION_CURRENCY")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RegionCurrency {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    private User user;

    private int amount;
    private String type; // 적립, 사용 등
    private String description;
    private LocalDateTime date;
} 