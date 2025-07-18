package com.ddasum.domain.payment.service;

import com.ddasum.domain.payment.dto.*;
import com.ddasum.domain.payment.entity.Payment;
import com.ddasum.domain.payment.repository.PaymentRepository;
import com.ddasum.domain.reservation.entity.Reservation;
import com.ddasum.domain.reservation.repository.ReservationRepository;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import jakarta.annotation.PostConstruct;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class PaymentServiceImpl implements PaymentService {
    private final PaymentRepository paymentRepository;
    private final ReservationRepository reservationRepository;
    private final ModelMapper modelMapper;
    private final IamportService iamportService;

    @PostConstruct
    public void configureModelMapper() {
        modelMapper.typeMap(Payment.class, PaymentResponse.class)
            .addMappings(mapper -> {
                mapper.map(src -> src.getReservation().getId(), PaymentResponse::setReservationId);
            });
    }

    @Override
    @Transactional
    public PaymentResponse create(PaymentRequest request) {
        // 아임포트 결제 검증
        Map paymentInfo = iamportService.verifyPayment(request.getImpUid());
        int paidAmount = (int) paymentInfo.get("amount");
        if (paidAmount != request.getAmount()) {
            throw new IllegalArgumentException("결제 금액이 일치하지 않습니다.");
        }
        Reservation reservation = reservationRepository.findById(request.getReservationId())
                .orElseThrow(() -> new IllegalArgumentException("예약을 찾을 수 없습니다."));
        Payment entity = Payment.builder()
                .reservation(reservation)
                .paymentMethod(request.getPaymentMethod())
                .amount(request.getAmount())
                .paymentDate(LocalDateTime.now())
                .build();
        Payment saved = paymentRepository.save(entity);
        return toResponse(saved);
    }

    @Override
    @Transactional
    public void cancel(String impUid, String reason) {
        Payment payment = paymentRepository.findByImpUid(impUid);
        if (payment == null) throw new IllegalArgumentException("결제를 찾을 수 없습니다.");
        iamportService.cancelPayment(impUid, payment.getAmount(), reason);
        // 필요시 payment 상태 업데이트 등 추가 구현
    }

    @Override
    public PaymentResponse getById(Long id) {
        Payment entity = paymentRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("결제를 찾을 수 없습니다."));
        return toResponse(entity);
    }

    @Override
    public List<PaymentResponse> getByReservationId(Long reservationId) {
        return paymentRepository.findByReservation_Id(reservationId).stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    @Override
    public PaymentResponse getByImpUid(String impUid) {
        Payment entity = paymentRepository.findByImpUid(impUid);
        if (entity == null) throw new IllegalArgumentException("결제를 찾을 수 없습니다.");
        return toResponse(entity);
    }

    @Override
    public PaymentResponse getByMerchantUid(String merchantUid) {
        Payment entity = paymentRepository.findByMerchantUid(merchantUid);
        if (entity == null) throw new IllegalArgumentException("결제를 찾을 수 없습니다.");
        return toResponse(entity);
    }

    @Override
    public List<PaymentResponse> getAll() {
        return paymentRepository.findAll().stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    private PaymentResponse toResponse(Payment entity) {
        return modelMapper.map(entity, PaymentResponse.class);
    }
} 