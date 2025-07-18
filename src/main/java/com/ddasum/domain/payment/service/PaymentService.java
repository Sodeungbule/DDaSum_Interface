package com.ddasum.domain.payment.service;

import com.ddasum.domain.payment.dto.*;
import java.util.List;

public interface PaymentService {
    PaymentResponse create(PaymentRequest request);
    void cancel(String impUid, String reason);
    PaymentResponse getById(Long id);
    List<PaymentResponse> getByReservationId(Long reservationId);
    PaymentResponse getByImpUid(String impUid);
    PaymentResponse getByMerchantUid(String merchantUid);
    List<PaymentResponse> getAll();
} 