package dev.be.springboot.java.async;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Component;

import java.util.concurrent.CompletableFuture;

@Slf4j
@Component
@RequiredArgsConstructor
public class CoffeeComponent implements CoffeeUseCase {

    private final CoffeeRepository coffeeRepository;
//    Executor executor = Executors.newFixedThreadPool(10);
    private final ThreadPoolTaskExecutor threadPoolTaskExecutor;


    @Override
    public int getPrice(String name) {
        log.info("동기 호출 방식으로 가격 조회");

        return coffeeRepository.getPriceByName(name);
    }

    // 비동기 - 1
//    @Override
//    public CompletableFuture<Integer> getPriceAsync(String name) {
//        log.info("비동기 호출 방식으로 가격 조회 시작");
//
//        CompletableFuture<Integer> future = new CompletableFuture<>();
//
//        new Thread(() -> {
//            log.info("새로운 스레드로 작업 시작");
//            Integer price = coffeeRepository.getPriceByName(name);
//            future.complete(price);
//        }).start();
//
//        return future;
//    }

    // 비동기 - 2
//    @Override
//    public CompletableFuture<Integer> getPriceAsync(String name) {
//        log.info("비동기 호출 방식으로 가격 조회 시작");
//
////        return CompletableFuture.supplyAsync(() -> coffeeRepository.getPriceByName(name));
//        return CompletableFuture.supplyAsync(() -> {
//            log.info("supplyAsync");
//            return coffeeRepository.getPriceByName(name);
//        });
//    }

    // 비동기 - 3
    @Override
    public CompletableFuture<Integer> getPriceAsync(String name) {
        log.info("비동기 호출 방식으로 가격 조회 시작");

//        return CompletableFuture.supplyAsync(() -> coffeeRepository.getPriceByName(name));
        return CompletableFuture.supplyAsync(() -> {
            log.info("supplyAsync");
            return coffeeRepository.getPriceByName(name);
        }, threadPoolTaskExecutor
        );
    }

    @Override
    public CompletableFuture<Integer> getDiscountAsync(int price) {
        log.info("비동기 호출 방식으로 할인율 적용");
        return CompletableFuture.supplyAsync(() -> {
            log.info("supplyAsync");
            return (int) (price * 0.9);
        }, threadPoolTaskExecutor);
    }
}
