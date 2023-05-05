package talk.messageService.chat

import io.kotest.core.spec.style.BehaviorSpec
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.flow
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.messaging.rsocket.RSocketRequester
import org.springframework.messaging.rsocket.dataWithType
import org.springframework.messaging.rsocket.retrieveFlow
import org.springframework.messaging.rsocket.retrieveFlux
import reactor.test.StepVerifier
import talk.messageService.chatMessage.ChatMessageVM
import java.time.Duration

@SpringBootTest
class ChatMessageResourceTest(
        private val rSocketRequester: RSocketRequester
) : BehaviorSpec({
    Given("chat 메세지 발신, 수신 stream 확인하기") {

        fun chatStream(id: String) = rSocketRequester.route("stream.chats.$id.message")

        When("chat 메세지 정상 발송한 경우") {
            Then("1개의 메세지가 정상 수신된다") {
                val payload = ChatMessageVM("a", "b")
                chatStream("a").dataWithType(
                        flow {
                            emit(payload)
                        }
                ).retrieveFlow<Unit>().collect()

                val received = chatStream("a").retrieveFlux<ChatMessageVM>()
                delay(1000)
                StepVerifier.create(received)
                        .expectNext(payload)
                        .thenCancel()
                        .verify(Duration.ofSeconds(2))
            }
        }
    }
})