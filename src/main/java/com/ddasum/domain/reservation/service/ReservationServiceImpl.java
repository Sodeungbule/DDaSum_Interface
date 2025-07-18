package com.ddasum.domain.reservation.service;

import com.ddasum.domain.reservation.dto.*;
import com.ddasum.domain.reservation.entity.Reservation;
import com.ddasum.domain.reservation.repository.ReservationRepository;
import com.ddasum.domain.user.entity.User;
import com.ddasum.domain.user.repository.UserRepository;
import com.ddasum.domain.vacanthouse.entity.VacantHouse;
import com.ddasum.domain.vacanthouse.repository.VacantHouseRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.stream.Collectors;
import org.modelmapper.ModelMapper;
import jakarta.annotation.PostConstruct;

@Service
@RequiredArgsConstructor
@Slf4j
public class ReservationServiceImpl implements ReservationService {
    private final ReservationRepository reservationRepository;
    private final UserRepository userRepository;
    private final VacantHouseRepository vacantHouseRepository;
    private final ModelMapper modelMapper;

    @PostConstruct
    public void configureModelMapper() {
        modelMapper.typeMap(Reservation.class, ReservationResponse.class)
            .addMappings(mapper -> {
                mapper.map(src -> src.getUser().getId(), ReservationResponse::setUserId);
                mapper.map(src -> src.getVacantHouse().getId(), ReservationResponse::setVacantHouseId);
            });
    }

    @Override
    public ReservationResponse createReservation(ReservationCreateRequest request) {
        User user = userRepository.findById(request.getUserId())
                .orElseThrow(() -> new IllegalArgumentException("사용자를 찾을 수 없습니다."));
        VacantHouse house = vacantHouseRepository.findById(request.getVacantHouseId())
                .orElseThrow(() -> new IllegalArgumentException("빈집을 찾을 수 없습니다."));
        Reservation entity = Reservation.builder()
                .user(user)
                .vacantHouse(house)
                .startDate(request.getStartDate())
                .endDate(request.getEndDate())
                .peopleCount(request.getPeopleCount())
                .status("진행중")
                .build();
        Reservation saved = reservationRepository.save(entity);
        return toResponse(saved);
    }

    @Override
    public ReservationResponse updateReservation(Long id, ReservationUpdateRequest request) {
        Reservation rs = reservationRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("예약을 찾을 수 없습니다."));
        rs.setStartDate(request.getStartDate());
        rs.setEndDate(request.getEndDate());
        rs.setPeopleCount(request.getPeopleCount());
        rs.setStatus(request.getStatus());
        Reservation updated = reservationRepository.save(rs);
        return toResponse(updated);
    }

    @Override
    public void deleteReservation(Long id) {
        reservationRepository.deleteById(id);
    }

    @Override
    public ReservationResponse getByReservationId(Long id) {
        Reservation entity = reservationRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("예약을 찾을 수 없습니다."));
        return toResponse(entity);
    }

    @Override
    public List<ReservationResponse> getByReservationAll() {
        return reservationRepository.findAll().stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    @Override
    public List<ReservationResponse> getByUserId(Long userId) {
        return reservationRepository.findByUserId(userId).stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    @Override
    public List<ReservationResponse> getByVacantHouseId(Long vacantHouseId) {
        return reservationRepository.findByVacantHouseId(vacantHouseId).stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    private ReservationResponse toResponse(Reservation entity) {
        return modelMapper.map(entity, ReservationResponse.class);
    }
} 