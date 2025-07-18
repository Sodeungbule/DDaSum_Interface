package com.ddasum.domain.reservation.repository;

import com.ddasum.domain.reservation.entity.Reservation;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ReservationRepository extends JpaRepository<Reservation, Long> {
    // 유저 ID로 예약 목록 조회
    List<Reservation> findByUserId(Long userId);
    // 빈집 ID로 예약 목록 조회
    List<Reservation> findByVacantHouseId(Long vacantHouseId);
}
