-- 1. 상품 (Product)
INSERT INTO product (id, name, thumbnail_url, description, short_description, cost_price, sales_price, discounted_price,
                     status, created_at, updated_at)
VALUES (1, '기모 후드티', 'https://static.toss.im/illusts/hoodie-gray.png', '따뜻한 기모 후드티입니다.', '기모 후드티', 20000, 30000, 25000,
        'ACTIVE', NOW(), NOW());

-- 2. 주문 (Order) - 결제 대기 상태 (CREATED)
INSERT INTO "order" (id, user_id, order_key, name, total_price, state, status, created_at, updated_at)
VALUES (1, 1, 'ORDER_TEST_HOODIE', '기모 후드티 외 0건', 25000, 'CREATED', 'ACTIVE', NOW(), NOW());

-- 3. 결제 (Payment) - 승인 대기 상태 (READY)
INSERT INTO payment (id, user_id, order_id, origin_amount, owned_coupon_id, coupon_discount, used_point, paid_amount,
                     state, external_payment_key, method, approve_code, paid_at, status, created_at, updated_at)
VALUES (1, 1, 1, 25000, 0, 0, 0, 25000, 'READY', NULL, NULL, NULL, NULL, 'ACTIVE', NOW(), NOW());

-- 4. 포인트 잔액 (PointBalance)
INSERT INTO point_balance (id, user_id, balance, version, status, created_at, updated_at)
VALUES (1, 1, 1000, 0, 'ACTIVE', NOW(), NOW());