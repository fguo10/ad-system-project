package com.example.adsponsor.entity;


import com.example.adsponsor.constant.CommonStatus;
import com.example.adsponsor.listeners.SharedEntityListener;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;


@Data
@AllArgsConstructor
@Entity
@EntityListeners(SharedEntityListener.class)
@Table(name = "ad_user")
public class AdUser {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @Column(name = "username", nullable = false)
    private String username;

    @Column(name = "token", nullable = false)
    private String token;

    @Column(name = "user_status", nullable = false)
    @Enumerated(EnumType.ORDINAL)
    private CommonStatus userStatus;

    @Column(name = "create_time", nullable = false)
    private LocalDateTime createTime;

    @Column(name = "update_time", nullable = false)
    private LocalDateTime updateTime;


    public AdUser() {
        this.userStatus = CommonStatus.VALID;
    }

    public AdUser(String username, String token) {
        this.username = username;
        this.token = token;
        this.userStatus = CommonStatus.VALID;
    }
}
