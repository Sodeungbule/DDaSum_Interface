package com.ddasum.domain.payment.repository;

import com.ddasum.domain.payment.entity.Payment;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface PaymentRepository extends JpaRepository<Payment, Long> {
    List<Payment> findByReservation_Id(Long reservationId);
    Payment findByImpUid(String impUid);
    Payment findByMerchantUid(String merchantUid);
} 