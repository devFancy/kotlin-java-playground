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
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
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
        // given
        String coffeeName = "americano";
        int expectedPrice = 2000;

        // when
        int resultPrice = coffeeComponent.getPrice(coffeeName);

        // then
        log.info("최종 가격 전달 받음");
        assertEquals(expectedPrice, resultPrice);
    }

    @Test
    public void 가격_조회_비동기_블로킹_호출_테스트() {
        // given
        String coffeeName = "americano";
        int expectedPrice = 2000;

        // when
        CompletableFuture<Integer> future = coffeeComponent.getPriceAsync(coffeeName);
        log.info("아직 최종 데이터를 전달 받지는 않았지만, 다른 작업 수행 가능");
        int resultPrice = future.join(); // 블로킹

        // then
        log.info("최종 가격 전달 받음");
        assertEquals(expectedPrice, resultPrice);
    }

    // 비동기 - thenAccept
    @Test
    public void 가격_조회_비동기_호출_콜백_반환없음_테스트() {
        // given
        String coffeeName = "americano";
        Integer expectedPrice = 2000;

        // when
        CompletableFuture<Void> future = coffeeComponent
                .getPriceAsync(coffeeName)
                .thenAccept(price -> {
                    // then
                    log.info("콜백, 가격은 {}원, 하지만 데이터를 반환하지는 않음", price);
                    assertEquals(expectedPrice, price);
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
        // given
        String coffeeName = "americano";
        Integer expectedPrice = 2000 + 100;
        Executor executor = Executors.newFixedThreadPool(5);

        // when
        CompletableFuture<Void> future = coffeeComponent
                .getPriceAsync(coffeeName)
                .thenApplyAsync(price -> {
                    log.info("다른 스레드로 동작");
                    return price + 100;
                }, executor)
                .thenAcceptAsync(price -> {
                    // then
                    log.info("콜백, 가격은 {}원, 하지만 데이터를 반환하지는 않음", price);
                    assertEquals(expectedPrice, price);
                }, executor);

        log.info("아직 최종 데이터를 전달 받지는 않았지만, 다른 작업 수행 가능");
        assertNull(future.join());
    }

    @Test
    public void thenCombine_test() {
        // given
        String coffeeA = "latte";
        String coffeeB = "mocha";
        Integer expectedPrice = 3000 + 4000;

        // when
        CompletableFuture<Integer> futureA = coffeeComponent.getPriceAsync(coffeeA);
        CompletableFuture<Integer> futureB = coffeeComponent.getPriceAsync(coffeeB);
        Integer resultPrice = futureA.thenCombine(futureB, Integer::sum).join();

        // then
        assertEquals(expectedPrice, resultPrice);
    }

    @Test
    public void thenCompose_test() {
        // given
        String coffeeName = "latte";
        Integer expectedPrice = (int) (3000 * 0.9);

        // when
        CompletableFuture<Integer> future = coffeeComponent.getPriceAsync(coffeeName);
        Integer resultPrice = future.thenCompose(price ->
                coffeeComponent.getDiscountAsync(price)).join();

        // then
        assertEquals(expectedPrice, resultPrice);
    }

    @Test
    public void allOf_test() {
        // given
        List<String> coffeeNames = Arrays.asList("latte", "mocha", "americano");
        Integer expectedPrice = 3000 + 4000 + 2000;

        // when
        List<CompletableFuture<Integer>> futures = coffeeNames.stream()
                .map(coffeeComponent::getPriceAsync)
                .collect(Collectors.toList());

        Integer resultPrice = CompletableFuture.allOf(futures.toArray(new CompletableFuture[0]))
                .thenApply(Void -> futures.stream()
                        .map(CompletableFuture::join)
                        .reduce(0, Integer::sum))
                .join();

        // then
        assertEquals(expectedPrice, resultPrice);
    }

    @Test
    public void 주문_비동기_호출_테스트() {
        // given
        String coffeeName = "latte";

        // when
        // 주문 호출
        coffeeComponent.order(coffeeName);


        // then
        log.info("Order method called (non-blocking)");
        // 테스트를 위해 비동기 작업이 완료될 때까지 대기
        try {
            Thread.sleep(4000); // 주문 처리 시간(3초) + 여유 시간
        } catch (InterruptedException e) {
            log.error("Test interrupted", e);
        }
        log.info("Test completed");
    }

    @Test
    public void 가격_조회_비동기_CompletableFuture_일부_논블로킹_테스트() throws Exception {
        // given
        String coffeeName = "latte";
        Integer expectedPrice = 3000;

        // when
        // 비동기 호출
        CompletableFuture<Integer> future = coffeeComponent.getPriceAsyncWithCompletableFuture(coffeeName);
        log.info("non-blocking");

        Integer blockingPrice = future.get(); // get 메서드를 실행하는 시점에서 다시 블로킹 현상이 발생함
        log.info("blocking: latte's price is " + blockingPrice);

        // then
        assertEquals("블로킹 호출 결과가 예상 가격과 다릅니다.", expectedPrice, blockingPrice);

        // 추가: 비동기 작업 콜백 확인
        future.thenAccept(price -> {
            log.info("latte's price is : " + price);
            assertEquals("콜백에서 반환된 가격이 예상 가격과 다릅니다.", expectedPrice, price);
        });

        log.info("after blocking");
    }

    /**
     * 전체 논블로킹 동작 과정을 테스트.
     *
     * Why? CompletableFuture 의 비동기 작업이 완료되기 전에 테스트 메서드가 종료될 수 있음.
     * - thenAccept 로 비동기 결과를 처리하지만, 테스트 프레임워크는 @Test의 끝을 메서드 종료로 간주.
     * - CountDownLatch 를 사용하여 비동기 작업 완료를 기다림.
     *
     * CountDownLatch 사용 이유
     * - 테스트 메서드가 비동기 작업 완료 신호를 받을 때까지 대기.
     * - 논블로킹 작업을 유지하면서 테스트 메서드 종료를 지연.
     * @throws Exception
     */
    @Test
    public void 가격_조회_비동기_CompletableFuture_전체_논블로킹_테스트() throws Exception {
        // given
        // CountDownLatch를 통해 비동기 작업 완료를 대기
        String coffeeName = "latte";
        CountDownLatch latch = new CountDownLatch(1);

        // when
        // 비동기 호출
        CompletableFuture<Integer> future = coffeeComponent.getPriceAsyncWithCompletableFuture(coffeeName);

        log.info("non-blocking 1: ...");

        // 콜백 등록: 작업 완료 시 결과를 처리
        future.thenAccept(p -> { // non-blocking 으로 동작하고 싶다면 콜백함수를 사용한다 -> thenAccept 메서드를 사용
            log.info("latte's price is : " + p);
            latch.countDown(); // 작업 완료 신호
        });

        log.info("non-blocking 2: ...");

        // then
        // 비동기 작업 완료 대기 (최대 5초)
        latch.await(5, TimeUnit.SECONDS); // 최대 5초 대기
        log.info("Test completed");
    }
}