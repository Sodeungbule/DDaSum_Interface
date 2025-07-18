package com.ddasum.domain.vacanthouse.service;

import com.ddasum.domain.vacanthouse.dto.VacantHouseCreateRequest;
import com.ddasum.domain.vacanthouse.dto.VacantHouseUpdateRequest;
import com.ddasum.domain.vacanthouse.dto.VacantHouseResponse;
import com.ddasum.domain.vacanthouse.entity.VacantHouse;
import com.ddasum.domain.vacanthouse.repository.VacantHouseRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.stream.Collectors;
import org.modelmapper.ModelMapper;

@Service
@RequiredArgsConstructor
public class VacantHouseServiceImpl implements VacantHouseService {
    private final VacantHouseRepository vacantHouseRepository;
    private final ModelMapper modelMapper;

    @Override
    public VacantHouseResponse create(VacantHouseCreateRequest request) {
        VacantHouse entity = VacantHouse.builder()
                .address(request.getAddress())
                .region(request.getRegion())
                .description(request.getDescription())
                .price(request.getPrice())
                .available(request.isAvailable())
                .imageUrl(request.getImageUrl())
                .build();
        VacantHouse saved = vacantHouseRepository.save(entity);
        return toResponse(saved);
    }

    @Override
    public VacantHouseResponse update(Long id, VacantHouseUpdateRequest request) {
        VacantHouse entity = vacantHouseRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("빈집을 찾을 수 없습니다."));
        entity.setAddress(request.getAddress());
        entity.setRegion(request.getRegion());
        entity.setDescription(request.getDescription());
        entity.setPrice(request.getPrice());
        entity.setAvailable(request.isAvailable());
        entity.setImageUrl(request.getImageUrl());
        VacantHouse updated = vacantHouseRepository.save(entity);
        return toResponse(updated);
    }

    @Override
    public void delete(Long id) {
        vacantHouseRepository.deleteById(id);
    }

    @Override
    public VacantHouseResponse getById(Long id) {
        VacantHouse entity = vacantHouseRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("빈집을 찾을 수 없습니다."));
        return toResponse(entity);
    }

    @Override
    public List<VacantHouseResponse> getAll() {
        return vacantHouseRepository.findAll().stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    private VacantHouseResponse toResponse(VacantHouse entity) {
        return modelMapper.map(entity, VacantHouseResponse.class);
    }
} 