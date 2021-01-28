> 안녕하세요. 해당 저장소는 배민플랫폼실 정산시스템팀 김시영의 파일럿 프로젝트 저장소입니다.
>
> 주제/ERD/요구사항/고민 등으로 정리되어 있으니 참고 바랍니다.

# 주제

배민플랫폼실 정산시스템의 간소화된 정산 Admin 시스템 개발하는 것이 주제입니다.

# ERD

![파일럿-ERD 2차](https://in.woowa.pilot.admin.security.in.woowa.pilot.admin.user-images.githubusercontent.com/49060374/103737240-25a52200-5035-11eb-9be6-bdd44886cb85.png)

# 실행 방법

1. 어드민 페이지

```
프로젝트를 클론 받은 이후 아래와 같이 실행하시면 됩니다.
oauth2 관련 설정은 같이 업로드되어있지 않습니다. 이 부분은 application-oauth.yml을 추가하셔서 실행하시면 될 것 같습니다

1. 터미널(한번에 실행)
    - ./gradlew clean
    - ./gradlew :pilot-admin:buildNeeded
    - cd pilot-admin
    - java -jar ./build/libs/pilot-admin-0.0.1-SNAPSHOT.jar
    - http://localhost:8080 접속
2. 프론트/백엔드 따로 실행
    - ./gradlew clean
    - ./gradlew :pilot-admin:buildNeeded
    - cd pilot-admin
    - java -jar ./build/libs/pilot-admin-0.0.1-SNAPSHOT.jar
    - cd frontend
    - npm start
    - http://localhost:3000 접속 
```

2. 배치 어플리케이션

```
    - ./gradlew clean
    - ./gradlew :pilot-batch:buildNeeded
    - cd pilot-batch
    - java -jar -Dspring.profiles.active=${profile} \
        ./build/libs/pilot-batch-0.0.1-SNAPSHOT.jar \
        --job.name=${job.name} parameters.... 
```

# 요구사항 분석

## 페이지 구성

- 로그인 및 회원가입 페이지
- 메인 페이지(업주/주문/보정금액/지급금)을 노출합니다.

## Pending

승인을 기다리는 요청들에 대한 정보를 담고 있습니다. 해당 엔티티는 `검증 알고리즘 + 배치`를 통해 제거될 수 있습니다.

- 트랜잭션 관리를 위해서 하나의 요청에 함께 사용하고 있습니다.

## 회원

회원은 우아한형제들 직원으로 한정하며 모든 직원은 정산어드민 페이지를 열람할 수 있습니다. 다만 관리자 권한은 승인 이후 특정 인물에게만 할당됩니다.

- 권한
    - 일반 사용자는 관리자 아닌 우아한형제들 직원을 의미하며 모든 부분에 있어, 조회/검색만 가능합니다.
    - 관리자는 어드민을 관리하는 직원을 의미하며 모든 데이터에 대해 CRUD 권한을 갖습니다.

### API

- 회원가입
    - 참고로 회원가입페이지는 두가지로 분류된다.
        - 구글로그인을 통해서 지원한다. (외부 API의 위치는 - 공동 서버(패키지))
        - 회원은 우아한형제들 직원으로 한정한다. `post /api/members/`
- 조회
    - 나의 정보보기 `get /api/members/`
        - 인가(본인의 정보만 조회할 수 있다)
- 수정
    - 나의 정보보기 → 수정하기 `put /api/members/`
    - 관리자 신청하기 → `patch /api/authority/members/admin`
        - 관리자 로그인 이후 승인 → `patch /api/members/admin`
        - **참고로 Pending에서도 검색 조건 명시**
- 삭제
    - 로그아웃 → 쿠키 날리기
    - 회원탈퇴(본인)→ Soft Delete `delete /api/member/

## B2B

B2B 고객이란 주문/보정금액/지급금 확인이 필요한 사람을 의미하며 사실상 `업주` 님들을 대상으로 합니다.

- 관리자만 업주를 생성/수정/삭제 할 수 있습니다.
- 생성/조회/삭제/수정는 `method /api/owners/`
- 조회(모든 회원이 조회할 수 있습니다.)
    - 개인정보조회(업주 정보 조회)

## 주문

주문이란 업주가 포스기와 같은 장비로 받은 주문데이터를 의미합니다.

- 주문 등록
    - 주문 등록은 관리자만 가능합니다. `post /api/orders/`
    - 주문 등록은 주문 상세와 함께 이루어집니다. `post /api/orders/`
        - 주문 등록과 주문 상세는 함께 이루어집니다. `cascade & orphanRemoval`
        - 트랜잭션관리에 유의
        - 주문 상세는 결제타입 및 금액을 갖습니다.
- 주문 검색(URL 이후 QueryParams 추가로 붙습니다)
    - 일시와 시간 기준  `get /api/orders/queryParams`
        - 일시를 기준으로 조회
        - 시간을 기준으로 조회
        - 일시 + 시간을 기준으로 조회
    - 업주 번호를 기준으로 조회 `get /api/orders/owners/{owner_id}`
- 주문 검색 및 조회시 주문 상세도 함께 조회할 수 있습니다.
    - 주문 상세 조회 `get /api/orders/{order_id}/order-details/{order_details_id}`

## 보상금액

보상금액이란 뜻밖의 이벤트로 인해 B2B고객(업주)님들께 보상 혹은 보정 해주는 금액을 의미합니다.

**보상금액에 있어서, 주문은 어떤 관계를 맺고 있는가?**

- 보상금액 신청하기(업주님들께 보이는 화면)
    - 업주정보/이유/언제/얼만큼 보상을 원하는지 설정
- 보상금액 등록하기(관리자에게만 보이는 화면 및 관리자만 가능)
    - `post /api/rewards/`
    - 신청한 보상금액들을 확인하고 이를 판단해 승인하는 형태
        - `get /api/authority/rewards/{reward_id}`
        - `delete /api/authority/rewards/{reward_id}`
        - `post /api/rewards/`
    - 보상금액을 직접 등록하는 형태  `post /api/rewards/`

## 지급금

지급이란 주문 데이터(주문 상세 데이터)와 보상금액을 통해 전체 업주가 받을 수 있는 금액을 계산한 데이터를 의미합니다. 지급금은 **일단위/주단위/월단위**로 계산되며 이에 해당 하는 금액을 `요청` 을 통해
계산하여 관리합니다.

- 지급금 생성 `post /api/settle`
- 지급금 조회 `get /api/settle/`
    - 어떤 주문들과 보상금액으로 해당 지급금이 조회되었는지 확인 할 수 있습니다.

# 할일 목록

- [x] 요구사항 분석 및 할일 목록 작성
- [x] 도메인 설계
    - [x] README.md에 ERD 추가
    - [x] 도메인 생성(with fields)

- [x] 기본 로직 개발(도메인 CRUD 및 기본 로직)
    - [x] 회원(Member) : 정산 Admin에 접근하는 우아한형제들 직원을 의미합니다.
        - [x] 구글 로그인
        - [x] 기본적인 CRUD
            - [x] 삭제는 soft delete
            - [x] 관리자 권한 요청 기능
        - [x] 로그인 통합 테스트
    - [x] 업주(Owner) : B2B 회원을 의미합니다.
        - [x] 사업자 번호를 통해 조회할 수 있습니다.
    - [x] 주문(Order) : 주문 1건에 대한 정보를 의미합니다.
    - [x] 주문상세(OrderDetail) : 주문 1건에 대한, 상세 정보를 의미하며 결제수단들을 포함합니다.
    - [x] 결제수단 및 각각의 할인율
    - [x] 보상(Reward) : 업주가 추가로 지불하거나, 받을 금액을 의미합니다.
    - [x] 지급(Settle) : 업주 및 전체가 지급 받을 수 있는 금액을 의미합니다.
        - [x] 생성 : 업주 별로 갖고 있는 주문 데이터의 합계 금액
            - 업주가 부담하는 쿠폰은 지급금에서 제외
            - 특정 기간에 속해 있는 주문과 보상금액을 지급금 대상에 포함할 수 있는지,
        - [x] 조회 : 지급급 조회시 지급금의 구성(주문들 + 보상금액) 을 함께 반환한다.

- [x] 추가 개발
    - [x] 리팩토링
        - [x] 테스트코드 리팩토링
        - [ ] 부족한 테스트 추가
        - [x] 프로덕션 코드 리팩토링
    - [ ] 예외처리
    - [x] build.gradle 정리
    - [ ] 캐시 적용
    - [ ] 권한관리 통합테스트
    - [x] Security Mocking 테스트
    - [x] API 문서화
    - [x] 관리자 회원에게 일반 회원 수정 및 삭제 API
    - [x] PaymentOption 리팩토링
    - [x] QueryDsl fetch null 반환 해결 
    