package ru.praktikumServices.stellarBurgers.model;

import org.apache.commons.lang3.RandomStringUtils;

public class RegisterUserRequest extends Request {
    public final String email;
    public final String password;
    public final String name;

    public RegisterUserRequest(String email, String password, String name) {
        this.email = email;
        this.password = password;
        this.name = name;
    }

    public static RegisterUserRequest getRandom() {
        final String name = RandomStringUtils.randomAlphabetic(10);
        final String email = RandomStringUtils.randomAlphabetic(10) + EMAIL_POSTFIX;
        final String password = RandomStringUtils.randomAlphabetic(10);
        return new RegisterUserRequest(email, password, name);
    }
}