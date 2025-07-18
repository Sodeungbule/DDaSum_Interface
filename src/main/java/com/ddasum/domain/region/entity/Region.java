package com.ddasum.domain.region.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "TBL_REGION")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Region {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private int population;
    private String transport;
    private String infra;
} 