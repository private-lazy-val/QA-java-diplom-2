package ru.praktikumServices.stellarBurgers.model;

public class GetSpecificUserOrdersRequest extends Request {

    public final String email;

    public GetSpecificUserOrdersRequest(String email) {
        this.email = email;
    }
}