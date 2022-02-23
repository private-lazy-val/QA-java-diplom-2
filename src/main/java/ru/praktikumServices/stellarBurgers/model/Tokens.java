package ru.praktikumServices.stellarBurgers.model;

public class Tokens {
    private static String accessToken;

    public static String getAccessToken() {
        return accessToken;
    }

    public static void setAccessToken(String accessToken) {
        Tokens.accessToken = accessToken;
    }

    public static void flush() {
        setAccessToken(null);
    }
}
