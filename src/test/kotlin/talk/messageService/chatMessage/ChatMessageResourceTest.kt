package talk.messageService.chatMessage

import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.matchers.shouldBe
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.reactor.asFlux
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.messaging.rsocket.RSocketRequester
import org.springframework.messaging.rsocket.dataWithType
import org.springframework.messaging.rsocket.retrieveFlow
import org.springframework.messaging.rsocket.retrieveFlux
import reactor.test.StepVerifier
import talk.messageService.config.TestSecurityConfig
import java.time.Duration
import java.util.concurrent.BlockingQueue
import java.util.concurrent.LinkedBlockingQueue


@SpringBootTest(
    classes = [TestSecurityConfig::class]
)
class ChatMessageResourceTest(
        private val rSocketRequester: RSocketRequester,
        private val repository: ChatMessageRepository,
        private val chatMessageStreamService: ChatMessageStreamService
) : BehaviorSpec({
    Given("chat 메세지 발신, 수신 stream 확인하기") {

        fun chatStream(id: String) = rSocketRequester.route("stream.chats.$id.message")

        When("chat 메세지 정상 발송한 경우") {
            beforeEach {
                repository.deleteAll()
            }
            Then("chatMessageStreamService > 1개의 메세지가 정상 수신된다") {
                val documents: BlockingQueue<ChatMessageVM> = LinkedBlockingQueue(100)
                val disposable = chatMessageStreamService.watchChanges("a")
                        .asFlux()
                        .doOnNext(documents::add)
                        .subscribe()
                val payload = ChatMessagePayload("a", "b")
                chatStream("a").dataWithType(
                        flow {
                            emit(payload)
                        }
                ).retrieveFlow<Unit>().collect()
                delay(1000)

                documents.size shouldBe 1
                disposable.dispose()
            }
            Then("1개의 메세지가 정상 수신된다") {
                val received = chatStream("a").retrieveFlux<ChatMessageVM>()
                val payload = ChatMessagePayload("a", "b")
                chatStream("a").dataWithType(
                        flow {
                            emit(payload)
                        }
                ).retrieveFlow<Unit>().collect()

                StepVerifier.create(received)
                        .expectNextCount(1)
                        .thenCancel()
                        .verify(Duration.ofSeconds(2))
            }
        }
    }
})