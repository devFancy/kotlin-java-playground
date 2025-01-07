package dev.be.springboot.java.async;

import java.util.concurrent.Future;

public interface CoffeeUseCase {
    int getPrice(String name); // 동기
    Future<Integer> getPriceAsync(String name); // 비동기
    Future<Integer> getDiscountAsync(int price); // 비동기
}
