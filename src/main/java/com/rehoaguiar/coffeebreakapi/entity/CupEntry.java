package com.rehoaguiar.coffeebreakapi.entity;

import jakarta.persistence.*;
import lombok.*;

/** Uma xícara tomada. Alimenta o Hoje, a cafeína do dia e o resumo. */
@Entity
@Table(name = "cup_entry",
        uniqueConstraints = @UniqueConstraint(name = "uk_cup_user_client", columnNames = {"user_id", "client_id"}),
        indexes = @Index(name = "ix_cup_user_ts", columnList = "user_id, ts"))
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CupEntry {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    /** Id gerado no app, para o envio ser idempotente (funciona offline). */
    @Column(name = "client_id", nullable = false, length = 64)
    private String clientId;

    /** Momento em que a xícara foi tomada, em milissegundos desde 1970 (UTC). */
    @Column(nullable = false)
    private Long ts;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private Integer mg;

    /** Tamanho da xícara ou caneca em ml (pó e grão). */
    private Integer ml;

    /** Gramas de café moído usados (grão). */
    private Integer grams;

    @Column(length = 20)
    private String color;

    @Column(name = "item_id", length = 120)
    private String itemId;

    @Column(name = "own_id", length = 64)
    private String ownId;
}
