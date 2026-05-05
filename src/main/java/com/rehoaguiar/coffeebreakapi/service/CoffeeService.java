package com.rehoaguiar.coffeebreakapi.service;

import com.rehoaguiar.coffeebreakapi.entity.Coffee;
import com.rehoaguiar.coffeebreakapi.exception.CoffeeNameAlreadyRegisteredException;
import com.rehoaguiar.coffeebreakapi.exception.CoffeeNotFoundException;
import com.rehoaguiar.coffeebreakapi.repository.CoffeeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CoffeeService {

    private final CoffeeRepository coffeeRepository;

    public Coffee create(Coffee coffee) {
        if (coffeeRepository.existsByName(coffee.getName())) {
            throw new CoffeeNameAlreadyRegisteredException("Já existe um café cadastrado com esse nome");
        }

        return coffeeRepository.save(coffee);
    }

    public List<Coffee> findAll() {
        return coffeeRepository.findAll();
    }

    public List<Coffee> findByType(String type) {
        return coffeeRepository.findByTypeIgnoreCase(type);
    }

    public Coffee findById(Long id) {
        return coffeeRepository.findById(id)
                .orElseThrow(() -> new CoffeeNotFoundException("Café não encontrado com o id: " + id));
    }

    public Coffee update(Long id, Coffee coffee) {
        Coffee existingCoffee = findById(id);

        if (coffeeRepository.existsByNameAndIdNot(coffee.getName(), id)) {
            throw new CoffeeNameAlreadyRegisteredException("Já existe um café cadastrado com esse nome");
        }

        existingCoffee.setName(coffee.getName());
        existingCoffee.setType(coffee.getType());
        existingCoffee.setBrand(coffee.getBrand());
        existingCoffee.setRoastLevel(coffee.getRoastLevel());
        existingCoffee.setFlavorNotes(coffee.getFlavorNotes());
        existingCoffee.setRating(coffee.getRating());
        existingCoffee.setDescription(coffee.getDescription());

        return coffeeRepository.save(existingCoffee);
    }

    public void delete(Long id) {
        Coffee coffee = findById(id);
        coffeeRepository.delete(coffee);
    }
}
