package dev.be.springboot.java.async;

import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import java.util.stream.Collectors;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

// JUnit 4에서는 테스트 클래스가 public 이어야 한다.
@Slf4j
@RunWith(SpringRunner.class)
@ContextConfiguration(classes = {
        CoffeeComponent.class,
        CoffeeRepository.class,
        TaskConfig.class
})
public class CoffeeComponentTest {

    @Autowired
    private CoffeeComponent coffeeComponent;

    @Test
    public void 가격_조회_동기_블로킹_호출_테스트() {

        int expectedPrice = 2000;

        int resultPrice = coffeeComponent.getPrice("americano");
        log.info("최종 가격 전달 받음");

        assertEquals(expectedPrice, resultPrice);
    }

    @Test
    public void 가격_조회_비동기_블로킹_호출_테스트() {

        int expectedPrice = 2000;

        CompletableFuture<Integer> future = coffeeComponent.getPriceAsync("americano");
        log.info("아직 최종 데이터를 전달 받지는 않았지만, 다른 작업 수행 가능");
        int resultPrice =  future.join(); // 블로킹
        log.info("최종 가격 전달 받음");

        assertEquals(expectedPrice, resultPrice);
    }

    // 비동기 - thenAccept
    @Test
    public void 가격_조회_비동기_호출_콜백_반환없음_테스트() {

        Integer expectedPrice = 2000;

        CompletableFuture<Void> future = coffeeComponent
                .getPriceAsync("americano")
                .thenAccept(p -> {
                    log.info("콜백, 가격은 " + p + "원, 하지만 데이터를 반환하지는 않음");
                    assertEquals(expectedPrice, p);
                });

        log.info("아직 최종 데이터를 전달 받지는 않았지만, 다른 작업 수행 가능, 논블로킹");
        /*
         아래 구문이 없으면 메인 스레드가 종료되기 때문에, thenAccept 확인하기 전에 끝난다.
         그래서 테스트를 위해 메인 스레드가 종료되지 않도록 블로킹으로 대기하기 위한 코드이다.
         future가 complete 가 되면 위에 작성한 thenAccept 코드가 실행이 된다.
         */
        assertNull(future.join());
    }

    // 비동기 - thenApply
    @Test
    public void 가격_조회_비동기_호출_콜백_반환_테스트() {

        Integer expectedPrice = 2000 + 100;
        Executor executor = Executors.newFixedThreadPool(5);

        CompletableFuture<Void> future = coffeeComponent
                .getPriceAsync("americano")
                .thenApplyAsync(p -> {
                    log.info("다른 스레드로 동작");
                    return p + 100;
                }, executor)
                .thenAcceptAsync(p -> {
                    log.info("콜백, 가격은 " + p + "원, 하지만 데이터를 반환하지는 않음");
                    assertEquals(expectedPrice, p);
                }, executor);

        log.info("아직 최종 데이터를 전달 받지는 않았지만, 다른 작업 수행 가능");
        assertNull(future.join());
    }

    @Test
    public void thenCombine_test() {

        Integer expectedPrice = 3000 + 4000;

        CompletableFuture<Integer> futureA = coffeeComponent.getPriceAsync("latte");
        CompletableFuture<Integer> futureB = coffeeComponent.getPriceAsync("mocha");

        // futureA.thenCombine(futureB, (a,b) -> a + b);
        Integer resultPrice = futureA.thenCombine(futureB, Integer::sum).join();

        assertEquals(expectedPrice, resultPrice);
    }

    @Test
    public void thenCompose_test() {

        Integer expectedPrice = (int) (3000 * 0.9);

        CompletableFuture<Integer> futureA = coffeeComponent.getPriceAsync("latte");

        Integer resultPrice = futureA.thenCompose(result ->
                coffeeComponent.getDiscountAsync(result)).join();

        assertEquals(expectedPrice, resultPrice);
    }

    @Test
    public void allOf_test() {

        Integer expectedPrice = 3000 + 4000 + 2000;

        CompletableFuture<Integer> futureA = coffeeComponent.getPriceAsync("latte");
        CompletableFuture<Integer> futureB = coffeeComponent.getPriceAsync("mocha");
        CompletableFuture<Integer> futureC = coffeeComponent.getPriceAsync("americano");

        List<CompletableFuture<Integer>> completableFutureList
                = Arrays.asList(futureA, futureB, futureC);

        // Integer resultPrice = CompletableFuture.allOf(completableFutureList.toArray(futureA, futureB, futureC));
        Integer resultPrice = CompletableFuture.allOf(futureA, futureB, futureC)
                .thenApply(Void -> completableFutureList.stream()
                        .map(CompletableFuture::join)
                        .collect(Collectors.toList()))
                .join()
                .stream()
                .reduce(0, Integer::sum);

        assertEquals(expectedPrice, resultPrice);
    }
}