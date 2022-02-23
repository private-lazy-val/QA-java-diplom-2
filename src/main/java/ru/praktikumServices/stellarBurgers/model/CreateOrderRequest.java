package ru.praktikumServices.stellarBurgers.model;

public class CreateOrderRequest extends Request {

    public final String[] ingredients;

    public CreateOrderRequest(String[] ingredients) {
        this.ingredients = ingredients;
    }
}