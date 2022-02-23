package ru.praktikumServices.stellarBurgers.model;

public class UpdateUserDataRequest extends Request {
    public final String email;
    public final String name;

    public UpdateUserDataRequest(String email, String name) {
        this.email = email;
        this.name = name;
    }
}