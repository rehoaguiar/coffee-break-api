package com.rehoaguiar.coffeebreakapi.service;

import com.rehoaguiar.coffeebreakapi.dto.CoffeeResponse;
import com.rehoaguiar.coffeebreakapi.dto.MeDtos.*;
import com.rehoaguiar.coffeebreakapi.entity.CupEntry;
import com.rehoaguiar.coffeebreakapi.entity.OwnCoffee;
import com.rehoaguiar.coffeebreakapi.entity.User;
import com.rehoaguiar.coffeebreakapi.entity.WishItem;
import com.rehoaguiar.coffeebreakapi.exception.UsernameAlreadyTakenException;
import com.rehoaguiar.coffeebreakapi.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/** Dados pessoais da conta logada: perfil, xícaras, meus cafés e lista de desejos. */
@Service
@RequiredArgsConstructor
public class MeService {
    private final CurrentUserService currentUser;
    private final UserRepository userRepository;
    private final CupEntryRepository entryRepository;
    private final OwnCoffeeRepository ownRepository;
    private final WishItemRepository wishRepository;
    private final CoffeeRepository coffeeRepository;

    @Transactional(readOnly = true)
    public MeDataResponse all() {
        User user = currentUser.get();
        return new MeDataResponse(
                ProfileResponse.fromEntity(user),
                entryRepository.findByUserOrderByTsAsc(user).stream().map(EntryResponse::fromEntity).toList(),
                coffeeRepository.findByUserOrderByCreatedAtDesc(user).stream().map(CoffeeResponse::fromEntity).toList(),
                ownRepository.findByUserOrderByIdAsc(user).stream().map(OwnCoffeeResponse::fromEntity).toList(),
                wishRepository.findByUserOrderByIdAsc(user).stream().map(WishItem::getItemId).toList()
        );
    }

    // ---------- perfil ----------

    @Transactional(readOnly = true)
    public ProfileResponse profile() {
        return ProfileResponse.fromEntity(currentUser.get());
    }

    @Transactional
    public ProfileResponse updateProfile(ProfileUpdateRequest req) {
        User user = currentUser.get();
        if (req.name() != null && !req.name().isBlank()) user.setName(req.name().trim());
        if (req.username() != null) {
            String u = req.username().trim().toLowerCase();
            if (userRepository.existsByUsernameIgnoreCaseAndIdNot(u, user.getId())) {
                throw new UsernameAlreadyTakenException("Esse @usuário já está em uso. Escolha outro.");
            }
            user.setUsername(u);
        }
        if (req.bio() != null) user.setBio(req.bio().trim());
        if (req.photo() != null) user.setPhoto(req.photo().isBlank() ? null : req.photo());
        if (req.refMg() != null) user.setRefMg(req.refMg());
        if (req.cutoff() != null) user.setCutoff(req.cutoff());
        if (req.publicBadges() != null) user.setPublicBadges(req.publicBadges());
        if (req.tastes() != null) {
            user.setTastes(String.join(",", req.tastes().stream().map(s -> s.replace(",", " ").trim()).filter(s -> !s.isEmpty()).toList()));
        }
        return ProfileResponse.fromEntity(userRepository.save(user));
    }

    // ---------- xícaras ----------

    @Transactional(readOnly = true)
    public List<EntryResponse> entries(Long from, Long to) {
        User user = currentUser.get();
        List<CupEntry> list = from != null || to != null
                ? entryRepository.findByUserAndTsBetweenOrderByTsAsc(user, from == null ? 0L : from, to == null ? Long.MAX_VALUE : to)
                : entryRepository.findByUserOrderByTsAsc(user);
        return list.stream().map(EntryResponse::fromEntity).toList();
    }

    @Transactional
    public EntryResponse putEntry(String clientId, EntryRequest req) {
        User user = currentUser.get();
        CupEntry e = entryRepository.findByUserAndClientId(user, clientId)
                .orElseGet(() -> CupEntry.builder().user(user).clientId(clientId).build());
        e.setTs(req.ts());
        e.setName(req.name().trim());
        e.setMg(req.mg());
        e.setColor(req.color());
        e.setItemId(req.itemId());
        e.setOwnId(req.ownId());
        return EntryResponse.fromEntity(entryRepository.save(e));
    }

    @Transactional
    public void deleteEntry(String clientId) {
        entryRepository.findByUserAndClientId(currentUser.get(), clientId).ifPresent(entryRepository::delete);
    }

    // ---------- meus cafés ----------

    @Transactional(readOnly = true)
    public List<OwnCoffeeResponse> ownCoffees() {
        return ownRepository.findByUserOrderByIdAsc(currentUser.get()).stream().map(OwnCoffeeResponse::fromEntity).toList();
    }

    @Transactional
    public OwnCoffeeResponse putOwnCoffee(String clientId, OwnCoffeeRequest req) {
        User user = currentUser.get();
        OwnCoffee o = ownRepository.findByUserAndClientId(user, clientId)
                .orElseGet(() -> OwnCoffee.builder().user(user).clientId(clientId).build());
        o.setName(req.name().trim());
        o.setKind(req.kind());
        o.setMg(req.mg());
        o.setRoast(req.roast());
        o.setColor(req.color());
        return OwnCoffeeResponse.fromEntity(ownRepository.save(o));
    }

    @Transactional
    public void deleteOwnCoffee(String clientId) {
        ownRepository.findByUserAndClientId(currentUser.get(), clientId).ifPresent(ownRepository::delete);
    }

    // ---------- lista de desejos ----------

    @Transactional(readOnly = true)
    public List<String> wishlist() {
        return wishRepository.findByUserOrderByIdAsc(currentUser.get()).stream().map(WishItem::getItemId).toList();
    }

    @Transactional
    public void addWish(String itemId) {
        User user = currentUser.get();
        if (wishRepository.findByUserAndItemId(user, itemId).isEmpty()) {
            wishRepository.save(WishItem.builder().user(user).itemId(itemId).build());
        }
    }

    @Transactional
    public void removeWish(String itemId) {
        wishRepository.findByUserAndItemId(currentUser.get(), itemId).ifPresent(wishRepository::delete);
    }
}
