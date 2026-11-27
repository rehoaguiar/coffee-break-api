package com.rehoaguiar.coffeebreakapi.service;

import com.rehoaguiar.coffeebreakapi.entity.Coffee;
import com.rehoaguiar.coffeebreakapi.entity.User;
import com.rehoaguiar.coffeebreakapi.exception.CoffeeNameAlreadyRegisteredException;
import com.rehoaguiar.coffeebreakapi.exception.CoffeeNotFoundException;
import com.rehoaguiar.coffeebreakapi.repository.CoffeeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/** Avaliações do caderninho. Tudo é sempre da conta logada. */
@Service
@RequiredArgsConstructor
public class CoffeeService {

    private static final String DUPLICATE = "Já existe um café cadastrado com esse nome";

    private final CoffeeRepository coffeeRepository;
    private final CurrentUserService currentUser;

    @Transactional
    public Coffee create(Coffee coffee) {
        User user = currentUser.get();
        if (coffeeRepository.existsByUserAndNameIgnoreCase(user, coffee.getName())) {
            throw new CoffeeNameAlreadyRegisteredException(DUPLICATE);
        }
        coffee.setUser(user);
        return coffeeRepository.save(coffee);
    }

    @Transactional(readOnly = true)
    public List<Coffee> findAll() {
        return coffeeRepository.findByUserOrderByCreatedAtDesc(currentUser.get());
    }

    @Transactional(readOnly = true)
    public List<Coffee> findByType(String type) {
        return coffeeRepository.findByUserAndTypeIgnoreCaseOrderByCreatedAtDesc(currentUser.get(), type);
    }

    @Transactional(readOnly = true)
    public Coffee findById(Long id) {
        return find(currentUser.get(), id);
    }

    @Transactional
    public Coffee update(Long id, Coffee coffee) {
        User user = currentUser.get();
        Coffee existing = find(user, id);
        if (coffeeRepository.existsByUserAndNameIgnoreCaseAndIdNot(user, coffee.getName(), id)) {
            throw new CoffeeNameAlreadyRegisteredException(DUPLICATE);
        }
        copy(coffee, existing);
        return coffeeRepository.save(existing);
    }

    /**
     * Cria ou atualiza a avaliação identificada pela chave do app.
     * Se ainda não existe pela chave mas existe uma com o mesmo nome, ela é reaproveitada.
     */
    @Transactional
    public Coffee upsertByKey(String key, Coffee coffee) {
        User user = currentUser.get();
        Coffee existing = coffeeRepository.findByUserAndClientKey(user, key)
                .or(() -> coffeeRepository.findFirstByUserAndNameIgnoreCase(user, coffee.getName()))
                .orElse(null);
        if (existing == null) {
            coffee.setUser(user);
            coffee.setClientKey(key);
            return coffeeRepository.save(coffee);
        }
        if (coffeeRepository.existsByUserAndNameIgnoreCaseAndIdNot(user, coffee.getName(), existing.getId())) {
            throw new CoffeeNameAlreadyRegisteredException(DUPLICATE);
        }
        copy(coffee, existing);
        existing.setClientKey(key);
        return coffeeRepository.save(existing);
    }

    @Transactional
    public void delete(Long id) {
        coffeeRepository.delete(find(currentUser.get(), id));
    }

    @Transactional
    public void deleteByKey(String key) {
        coffeeRepository.findByUserAndClientKey(currentUser.get(), key).ifPresent(coffeeRepository::delete);
    }

    private Coffee find(User user, Long id) {
        return coffeeRepository.findByIdAndUser(id, user)
                .orElseThrow(() -> new CoffeeNotFoundException("Café não encontrado com o id: " + id));
    }

    private static void copy(Coffee from, Coffee to) {
        to.setName(from.getName());
        to.setType(from.getType());
        to.setBrand(from.getBrand());
        to.setRoastLevel(from.getRoastLevel());
        to.setFlavorNotes(from.getFlavorNotes());
        to.setRating(from.getRating());
        to.setDescription(from.getDescription());
        to.setItemId(from.getItemId());
        to.setColor(from.getColor());
    }
}
