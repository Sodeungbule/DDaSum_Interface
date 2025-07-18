package com.ddasum.domain.payment.entity;

import com.ddasum.domain.reservation.entity.Reservation;
import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "TBL_PAYMENT")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Payment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(fetch = FetchType.LAZY)
    private Reservation reservation;

    private String paymentMethod;
    private int amount;
    private LocalDateTime paymentDate;
    private String impUid;      // 아임포트 결제 고유번호
    private String merchantUid; // 가맹점 주문번호
} 