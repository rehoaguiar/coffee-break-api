package com.rehoaguiar.coffeebreakapi.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "users")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class User {
    public static final int DEFAULT_REF_MG = 400;
    public static final int DEFAULT_CUTOFF = 18;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    private String name;

    @Email
    @NotBlank
    @Column(unique = true, nullable = false)
    private String email;

    @NotBlank
    @Column(nullable = false)
    private String password;

    /** Nome de usuário público, único. */
    @Column(unique = true, length = 40)
    private String username;

    @Column(length = 300)
    private String bio;

    /** Foto de perfil como data URI (base64), já reduzida pelo app. */
    @Column(name = "photo", length = 1_500_000)
    private String photo;

    /** Referência diária de cafeína em mg. */
    @Column(name = "ref_mg")
    private Integer refMg;

    /** Hora de corte da cafeína (0 a 23). */
    private Integer cutoff;

    @Column(name = "public_badges")
    private Boolean publicBadges;

    /** Gostos escolhidos no cadastro, separados por vírgula. */
    @Column(length = 500)
    private String tastes;

    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    @PrePersist
    public void prePersist() {
        if (createdAt == null) createdAt = LocalDateTime.now();
        if (refMg == null) refMg = DEFAULT_REF_MG;
        if (cutoff == null) cutoff = DEFAULT_CUTOFF;
        if (publicBadges == null) publicBadges = true;
    }
}
