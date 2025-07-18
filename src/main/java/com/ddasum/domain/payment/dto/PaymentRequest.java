package com.ddasum.domain.payment.dto;
import lombok.Data;

@Data
public class PaymentRequest {
    private Long reservationId;
    private int amount;
    private String paymentMethod;
    private String impUid; // 아임포트 결제 고유번호
    private String merchantUid; // 가맹점 주문번호
} 