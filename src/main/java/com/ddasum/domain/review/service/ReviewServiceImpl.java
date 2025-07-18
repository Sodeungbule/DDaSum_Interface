package com.ddasum.domain.review.service;

import com.ddasum.domain.review.dto.*;
import com.ddasum.domain.review.entity.Review;
import com.ddasum.domain.review.repository.ReviewRepository;
import com.ddasum.domain.user.entity.User;
import com.ddasum.domain.user.repository.UserRepository;
import com.ddasum.domain.vacanthouse.entity.VacantHouse;
import com.ddasum.domain.vacanthouse.repository.VacantHouseRepository;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import jakarta.annotation.PostConstruct;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ReviewServiceImpl implements ReviewService {
    private final ReviewRepository reviewRepository;
    private final UserRepository userRepository;
    private final VacantHouseRepository vacantHouseRepository;
    private final ModelMapper modelMapper;

    @PostConstruct
    public void configureModelMapper() {
        modelMapper.typeMap(Review.class, ReviewResponse.class)
            .addMappings(mapper -> {
                mapper.map(src -> src.getUser().getId(), ReviewResponse::setUserId);
                mapper.map(src -> src.getVacantHouse().getId(), ReviewResponse::setVacantHouseId);
            });
    }

    @Override
    public ReviewResponse createReview(ReviewCreateRequest request) {
        User user = userRepository.findById(request.getUserId())
                .orElseThrow(() -> new IllegalArgumentException("사용자를 찾을 수 없습니다."));
        VacantHouse house = vacantHouseRepository.findById(request.getVacantHouseId())
                .orElseThrow(() -> new IllegalArgumentException("빈집을 찾을 수 없습니다."));
        Review entity = Review.builder()
                .user(user)
                .vacantHouse(house)
                .rating(request.getRating())
                .content(request.getContent())
                .imageUrl(request.getImageUrl())
                .build();
        Review saved = reviewRepository.save(entity);
        return toResponse(saved);
    }

    @Override
    public ReviewResponse updateReview(Long id, ReviewUpdateRequest request) {
        Review entity = reviewRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("리뷰를 찾을 수 없습니다."));
        entity.setRating(request.getRating());
        entity.setContent(request.getContent());
        entity.setImageUrl(request.getImageUrl());
        Review updated = reviewRepository.save(entity);
        return toResponse(updated);
    }

    @Override
    public void deleteReview(Long id) {
        reviewRepository.deleteById(id);
    }

    @Override
    public ReviewResponse getByReviewId(Long id) {
        Review entity = reviewRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("리뷰를 찾을 수 없습니다."));
        return toResponse(entity);
    }

    @Override
    public List<ReviewResponse> getByReviewAll() {
        return reviewRepository.findAll().stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    @Override
    public List<ReviewResponse> getByUserId(Long userId) {
        return reviewRepository.findByUserId(userId).stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    @Override
    public List<ReviewResponse> getByVacantHouseId(Long vacantHouseId) {
        return reviewRepository.findByVacantHouseId(vacantHouseId).stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    private ReviewResponse toResponse(Review entity) {
        return modelMapper.map(entity, ReviewResponse.class);
    }
} 