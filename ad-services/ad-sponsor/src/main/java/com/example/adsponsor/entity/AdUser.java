package com.example.adsponsor.entity;


import com.example.adsponsor.constant.CommonStatus;
import com.example.adsponsor.listeners.SharedEntityListener;
import jakarta.persistence.*;
import lombok.*;



@Data
@NoArgsConstructor
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

    public AdUser(String username, String token, Integer userStatus) {
        this.username = username;
        this.token = token;
    }
}
