package talk.messageService.chatMessage

import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.matchers.comparables.shouldBeGreaterThan
import io.kotest.matchers.nulls.shouldNotBeNull
import io.kotest.matchers.should
import io.kotest.matchers.shouldBe
import io.kotest.matchers.shouldNotBe
import kotlinx.coroutines.flow.firstOrNull
import talk.messageService.chatMessage.ChatMessage.Companion.chatMessage
import talk.messageService.mongo.RepositoryTest

@RepositoryTest
class ChatMessageRepositoryTests(
        private val repository: ChatMessageRepository
) : BehaviorSpec({
    Given("chat-message 데이터 생성") {
        When("데이터 생성 성공한 경우") {
            beforeTest {
                chatMessage {
                    this.chatId = "a"
                    this.chatParticipantId = "b"
                    this.content = "c"
                }.run { repository.save(this) }
            }
            Then("database 에 데이터가 존재한다") {
                repository.findAll().firstOrNull().shouldNotBeNull().should {
                    it.id shouldNotBe null
                    it.chatId.length shouldBeGreaterThan 0
                    it.chatParticipantId.length shouldBeGreaterThan 0
                    it.content.length shouldBeGreaterThan 0
                    it.createdAt shouldNotBe null
                }
            }
        }
    }
})