package com.example.adsponsor.entity.adunit_condition;

import jakarta.persistence.*;
import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "ad_unit_district")
public class AdUnitDistrict {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    
    @Column(name = "unit_id", nullable = false)
    private Long unitId;

    
    @Column(name = "state", nullable = false)
    private String state;

    
    @Column(name = "city", nullable = false)
    private String city;

    public AdUnitDistrict(Long unitId, String state, String city) {
        this.unitId = unitId;
        this.state = state;
        this.city = city;
    }
}
