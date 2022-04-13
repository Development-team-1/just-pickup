<h1>
■ [개발 1팀] Just Pickup - 커피 픽업 서비스
</h1>

## 😀 참가 맴버

<table>
    <tr>
        <td align="center"><a href="https://github.com/bum12ark"><img src="https://avatars.githubusercontent.com/u/72686708?v=4?s=100" width="100px;" alt="" /><br /><sub><b>bum12ark</b></sub></a><br /></td>
        <td align="center"><a href="https://github.com/hoon7566"><img src="https://avatars.githubusercontent.com/u/48042490?v=4?s=100" width="100px;" alt="" /><br /><sub><b>hoon7566</b></sub></a><br /></td>
    </tr>
</table>

## 📋 설계서

### 이벤트 스토밍 (Event Storming)

#### 초기 단계
<table>
    <tr>
        <td align="center">
            <img src="https://user-images.githubusercontent.com/72686708/150717364-6a6d04a6-89bc-42c2-b3a7-8634fc86bed2.jpg" width="500" height="500"/>
        </td>
        <td align="center">
            <img src="https://user-images.githubusercontent.com/72686708/150717369-fed92a58-d27b-4126-9709-80b16f6c930d.jpg" width="500" height="500"/>
        </td>
    </tr>
</table>

#### 애그리게이트 분리 단계
<table>
    <tr>
        <td align="center">
            <img src="https://user-images.githubusercontent.com/48042490/150772741-c17fd353-01eb-43ee-853c-a2ebd9224f23.jpg" width="500" height="500"/>
        </td>
    </tr>
</table>

### DB 설계

#### 물리 모델 (Physical model)
<table>
    <tr>
        <td align="center">            
            <img src="https://user-images.githubusercontent.com/48042490/150922528-21c6c430-a9d6-4e03-a450-3d4d22e62126.png" width="500" height="500"/>
        </td>
    </tr>
</table>

#### 논리 모델 (Logical model)

<table>
    <tr>
        <td align="center">            
            <img src="https://user-images.githubusercontent.com/72686708/151095981-4222b765-ae8e-46f5-9289-f9189d4c1b28.png" width="500" height="500"/>
        </td>
    </tr>
</table>

### API 설계 ✏️
| 서비스 | 설계서 |
| --- | --- |
| USER-SERVICE |[링크](https://development-team-1.github.io/just-pickup/User-Service-API-%EB%AC%B8%EC%84%9C/)|
| STORE-SERVICE |[링크](https://development-team-1.github.io/just-pickup/Store-Service-API-%E1%84%86%E1%85%AE%E1%86%AB%E1%84%89%E1%85%A5/)|
| ORDER-SERVICE |[링크](https://development-team-1.github.io/just-pickup/Order-Service-API-%E1%84%86%E1%85%AE%E1%86%AB%E1%84%89%E1%85%A5/)|
| NOTIFICATION-SERVICE |[링크](https://development-team-1.github.io/just-pickup/Notification-Service-API-%EB%AC%B8%EC%84%9C/)|

### Overview 🔎
| 서비스 | PDF |
| --- | --- |
|사용자|[링크](https://github.com/Development-team-1/just-pickup/blob/master/docs/overview/Just_Pickup.pdf)|
|점주|[링크](https://github.com/Development-team-1/just-pickup/blob/master/docs/overview/%E1%84%8C%E1%85%A5%E1%86%B7%E1%84%8C%E1%85%AE%E1%84%8B%E1%85%AD%E1%86%BC%20%E1%84%80%E1%85%A1%E1%84%8B%E1%85%B5%E1%84%83%E1%85%B3.pdf)|

## 아키텍처
| 기술스택 | 개발환경 |
| --- | --- |
| Spring Boot | - Spring Framwork 2.6.3 </br> - Java 11 </br> - Gradle </br> - Spring Web Mvc </br> - Spring Security |
| Spring Cloud | - Eureka </br> - Gateway </br> - OpenFeign </br> - Config </br> - Redis Rate Limiter |
| Authenticate | - JWT (Json Web Token) </br> - OAuth 2.0 |
| ORM | - JPA </br> - QueryDsl |
| Message Queue | - Kafka |
| Database | - PostgreSQL </br> - Redis  |
| Test | - Spring RestDocs |
| 모니터링 | - Zipkin </br> - Spring Cloud Sleuth |
| Vue | - Vue-Router </br> - axios </br> - Vuetify |
 ```
1. JWT 토큰을 이용한 로그인, 회원가입 구현
2. Kafka를 사용해 이벤트 드리븐 아키텍처 구현
3. RestDocs를 이용한 테스트 작성 및 API 문서 작성
4. Open Feign을 이용한 인터페이스 형식의 HTTP 통신 구현 
5. reactive redis를 사용하여 api 호출 과부화를 막는 rate limiter 구현
```

### 시스템 아키텍처
![system architecture](https://user-images.githubusercontent.com/72686708/161487968-9d8795be-efdd-4f2d-97ea-d2c21ecaf5fb.png)

### Microservice 통신
![microservice](https://user-images.githubusercontent.com/72686708/161488158-66a9bc1c-7757-4062-b46e-0e14005e505a.png)


## 주요 이슈
- [Feign Client 다중 호출 성능 최적기](https://github.com/Development-team-1/just-pickup/issues/61)

## build & run

