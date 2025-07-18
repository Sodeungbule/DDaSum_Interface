package com.ddasum.domain.payment.dto;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PaymentResponse {
    private Long id;
    private Long reservationId;
    private String paymentMethod;
    private int amount;
    private LocalDateTime paymentDate;
    private String impUid;
    private String merchantUid;
} 