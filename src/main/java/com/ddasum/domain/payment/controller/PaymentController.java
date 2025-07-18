package com.ddasum.domain.payment.controller;

import com.ddasum.core.api.response.ApiResponse;
import com.ddasum.core.annotation.LoginRequired;
import com.ddasum.core.annotation.TraceLog;
import com.ddasum.core.controller.BaseController;
import com.ddasum.domain.payment.dto.*;
import com.ddasum.domain.payment.service.PaymentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@LoginRequired
@TraceLog
@RestController
@RequestMapping("/api/v1/payments")
@RequiredArgsConstructor
public class PaymentController extends BaseController {
    private final PaymentService paymentService;

    @PostMapping
    public ResponseEntity<ApiResponse<PaymentResponse>> create(@RequestBody PaymentRequest request) {
        return success(paymentService.create(request));
    }

    @PostMapping("/cancel")
    public ResponseEntity<ApiResponse<Void>> cancel(@RequestParam String impUid, @RequestParam String reason) {
        paymentService.cancel(impUid, reason);
        return success(null);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<PaymentResponse>> getById(@PathVariable Long id) {
        return success(paymentService.getById(id));
    }

    @GetMapping
    public ResponseEntity<ApiResponse<List<PaymentResponse>>> getAll() {
        return success(paymentService.getAll());
    }

    @GetMapping("/reservation/{reservationId}")
    public ResponseEntity<ApiResponse<List<PaymentResponse>>> getByReservationId(@PathVariable Long reservationId) {
        return success(paymentService.getByReservationId(reservationId));
    }

    @GetMapping("/imp-uid/{impUid}")
    public ResponseEntity<ApiResponse<PaymentResponse>> getByImpUid(@PathVariable String impUid) {
        return success(paymentService.getByImpUid(impUid));
    }

    @GetMapping("/merchant-uid/{merchantUid}")
    public ResponseEntity<ApiResponse<PaymentResponse>> getByMerchantUid(@PathVariable String merchantUid) {
        return success(paymentService.getByMerchantUid(merchantUid));
    }
} 