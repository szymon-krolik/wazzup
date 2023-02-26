package com.wazzup.entity;


import lombok.Data;

import javax.persistence.*;
import java.time.Instant;

/**
 * @author Szymon Królik
 */
@Data
@Entity
@Table(name = "REFRESG_TOKEN")
public class RefreshToken {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "TOKEN_ID")
    private Long id;

    @Column(unique = true, nullable = false, name = "TOKEN")
    private String token;

    @Column(nullable = false, name = "EXPIRATION_DATE")
    private Instant expirationDate;

    @OneToOne
    @JoinColumn(name = "USER_ID", referencedColumnName = "USER_ID")
    private User user;
}
