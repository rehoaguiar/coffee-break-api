package com.rehoaguiar.coffeebreakapi.exception;

public class CoffeeNameAlreadyRegisteredException extends RuntimeException {
    public CoffeeNameAlreadyRegisteredException(String message) {
        super(message);
    }
}
