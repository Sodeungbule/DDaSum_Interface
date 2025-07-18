package com.ddasum.domain.vacanthouse.service;

import com.ddasum.domain.vacanthouse.dto.VacantHouseCreateRequest;
import com.ddasum.domain.vacanthouse.dto.VacantHouseUpdateRequest;
import com.ddasum.domain.vacanthouse.dto.VacantHouseResponse;
import java.util.List;

public interface VacantHouseService {
    VacantHouseResponse create(VacantHouseCreateRequest request);
    VacantHouseResponse update(Long id, VacantHouseUpdateRequest request);
    void delete(Long id);
    VacantHouseResponse getById(Long id);
    List<VacantHouseResponse> getAll();
} 