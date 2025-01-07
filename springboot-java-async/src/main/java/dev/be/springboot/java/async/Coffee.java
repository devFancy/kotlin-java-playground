package dev.be.springboot.java.async;

import lombok.Builder;

public class Coffee {

    private String name;
    private int price;

    @Builder
    public Coffee(String name, int price) {
        this.name = name;
        this.price = price;
    }

    public String getName() {
        return name;
    }

    public int getPrice() {
        return price;
    }
}
