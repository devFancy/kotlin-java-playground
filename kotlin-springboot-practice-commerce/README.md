# Dev Practice - Commerce

## Development environment setup

### Git Hook
This setting makes run `lint` on every commit.

```
$ git config core.hookspath .githooks
```

### IntelliJ IDEA
This setting makes it easier to run the `test code` out of the box.

```
// Gradle Build and run with IntelliJ IDEA
Build, Execution, Deployment > Build Tools > Gradle > Run tests using > IntelliJ IDEA	
```


---


# Payment(결제)

## 외부 연동 가이드

외부 결제 API(PG사) 연동 테스트를 위해 [Beeceptor](https://app.beeceptor.com/)를 사용하여 Mock Server를 구축했습니다.

로컬 개발 환경에서는 실제 결제 승인 대신 Beeceptor를 호출하여 승인/실패 로직을 테스트합니다.

### 1. Beeceptor Mocking Rule 설정

Beeceptor Console > Mocking Rules 메뉴에서 아래 규칙을 등록해야 합니다.

- Base URL: `https://commerce.free.beeceptor.com` (본인의 Beeceptor 도메인으로 변경 필요)
- Target URL Path: `/api/v1/payment`
- Method: `POST`

#### Case 1: 결제 승인 성공 (Success)

정상적으로 결제가 승인되었을 때 PG사로부터 받는 응답 예시입니다.

- Status Code: `200 OK`
- Response Body (JSON):

```json
{
  "externalPaymentKey": "payment_key_test",
  "method": "CARD",
  "approveNo": "12345678",
  "message": "결제 성공"
}
```

#### Case 2: 결제 승인 실패 (Failure)

잔액 부족, 한도 초과, 네트워크 오류 등의 상황을 시뮬레이션합니다.

- Status Code: 400 Bad Request (또는 500)
- Response Body (JSON):

```json
{
  "code": "PAYMENT_REJECTED",
  "message": "잔액이 부족합니다."
}
```

### 2. 로컬 테스트 데이터

서버 실행 시 테스트용 주문 데이터(ORDER_TEST_HOODIE)가 자동으로 생성됩니다. (상태: Order=CREATED, Payment=READY)

### 3. API 테스트 (HTTP Request)

아래 요청을 통해 결제 승인 및 실패 로직을 검증할 수 있습니다.

(참고: H2 DB 특성상, 성공 테스트 후 실패 테스트를 하려면 서버를 재시작하여 데이터를 초기화해야 합니다.)

```http request
### 1. 결제 승인 (성공 케이스)
# Beeceptor 규칙: Case 1 활성화 (200 OK)
POST http://localhost:8080/v1/payments/callback/success?orderId=ORDER_TEST_HOODIE&paymentKey=payment_key_test&amount=25000
Content-Type: application/json

### 2. 결제 승인 (실패 케이스 - 잔액 부족 등)
# Beeceptor 규칙: Case 2 활성화 (400 Error)
POST http://localhost:8080/v1/payments/callback/fail?orderId=ORDER_TEST_HOODIE&code=PAYMENT_REJECTED&message=잔액부족
Content-Type: application/json
```

