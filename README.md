# talk-message-service-2023
- 채팅방 메세지를 처리하는 서버 입니다.
- rsocket 의 비동기 메세지 모델 `fire and forgot` `request stream` 을 이용하여 메세지 발신 | 수신 로직을 구현
- mongodb change stream 을 이용하여 데이터의 실시간 변경을 감지
## framework
- rsocket
- reactive-mongo-client

