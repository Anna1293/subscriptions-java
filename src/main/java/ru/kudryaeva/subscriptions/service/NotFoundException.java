package ru.kudryaeva.subscriptions.service;

public class NotFoundException extends RuntimeException {

    public NotFoundException() {
        super("not found");
    }
}
