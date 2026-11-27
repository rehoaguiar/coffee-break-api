package com.rehoaguiar.coffeebreakapi.dto;

import com.rehoaguiar.coffeebreakapi.entity.CupEntry;
import com.rehoaguiar.coffeebreakapi.entity.OwnCoffee;
import com.rehoaguiar.coffeebreakapi.entity.User;
import com.rehoaguiar.coffeebreakapi.enums.RoastLevel;
import jakarta.validation.constraints.*;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

/** DTOs dos dados pessoais (perfil, xícaras, meus cafés, lista de desejos). */
public final class MeDtos {
    private MeDtos() {
    }

    public record ProfileResponse(
            String name,
            String email,
            String username,
            String bio,
            String photo,
            int refMg,
            int cutoff,
            boolean publicBadges,
            List<String> tastes,
            LocalDateTime createdAt
    ) {
        public static ProfileResponse fromEntity(User u) {
            List<String> tastes = u.getTastes() == null || u.getTastes().isBlank()
                    ? List.of()
                    : Arrays.stream(u.getTastes().split(",")).map(String::trim).filter(s -> !s.isEmpty()).toList();
            return new ProfileResponse(
                    u.getName(),
                    u.getEmail(),
                    u.getUsername(),
                    u.getBio(),
                    u.getPhoto(),
                    u.getRefMg() == null ? User.DEFAULT_REF_MG : u.getRefMg(),
                    u.getCutoff() == null ? User.DEFAULT_CUTOFF : u.getCutoff(),
                    u.getPublicBadges() == null || u.getPublicBadges(),
                    tastes,
                    u.getCreatedAt()
            );
        }
    }

    /** Atualização parcial do perfil: só os campos enviados mudam. Foto vazia ("") remove a foto. */
    public record ProfileUpdateRequest(
            @Size(min = 1, max = 120) String name,
            @Size(min = 2, max = 30) @Pattern(regexp = "^[a-z0-9._]+$", message = "use letras minúsculas, números, ponto ou _") String username,
            @Size(max = 300) String bio,
            @Size(max = 1_400_000) String photo,
            @Min(50) @Max(1000) Integer refMg,
            @Min(0) @Max(23) Integer cutoff,
            Boolean publicBadges,
            @Size(max = 20) List<@Size(max = 40) String> tastes
    ) {
    }

    public record EntryRequest(
            @NotNull Long ts,
            @NotBlank @Size(max = 255) String name,
            @NotNull @Min(0) @Max(1000) Integer mg,
            @Min(10) @Max(2000) Integer ml,
            @Min(1) @Max(200) Integer grams,
            @Size(max = 20) String color,
            @Size(max = 120) String itemId,
            @Size(max = 64) String ownId
    ) {
    }

    public record EntryResponse(String id, long ts, String name, int mg, Integer ml, Integer grams, String color, String itemId, String ownId) {
        public static EntryResponse fromEntity(CupEntry e) {
            return new EntryResponse(e.getClientId(), e.getTs(), e.getName(), e.getMg(), e.getMl(), e.getGrams(), e.getColor(), e.getItemId(), e.getOwnId());
        }
    }

    public record OwnCoffeeRequest(
            @NotBlank @Size(max = 255) String name,
            @NotBlank @Size(max = 255) String kind,
            @NotNull @Min(0) @Max(1000) Integer mg,
            @NotNull RoastLevel roast,
            @Size(max = 20) String color
    ) {
    }

    public record OwnCoffeeResponse(String id, String name, String kind, int mg, RoastLevel roast, String color) {
        public static OwnCoffeeResponse fromEntity(OwnCoffee o) {
            return new OwnCoffeeResponse(o.getClientId(), o.getName(), o.getKind(), o.getMg(), o.getRoast(), o.getColor());
        }
    }

    /** Tudo o que a conta tem, numa chamada só (usado ao entrar no app). */
    public record MeDataResponse(
            ProfileResponse profile,
            List<EntryResponse> entries,
            List<CoffeeResponse> reviews,
            List<OwnCoffeeResponse> mine,
            List<String> wish
    ) {
    }
}
