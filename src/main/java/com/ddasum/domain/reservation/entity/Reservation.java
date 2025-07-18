package com.ddasum.domain.reservation.entity;

import com.ddasum.domain.user.entity.User;
import com.ddasum.domain.vacanthouse.entity.VacantHouse;
import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDate;

@Entity
@Table(name = "TBL_RESERVATION")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Reservation {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    private VacantHouse vacantHouse;

    private LocalDate startDate;
    private LocalDate endDate;
    private int peopleCount;
    private String status; // 진행중, 완료, 취소 등
} 