package ru.praktikumServices.stellarBurgers.model;

import ru.praktikumServices.stellarBurgers.Utils;

public class Request {
    public static final String EMAIL_POSTFIX = "@yandex.ru";

    public String toJsonString() {
        return Utils.serializeToJsonIgnoreNulls(this);
    }
}
