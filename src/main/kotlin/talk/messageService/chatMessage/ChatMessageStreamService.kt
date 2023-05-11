package talk.messageService.chatMessage

import com.mongodb.client.model.changestream.OperationType
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.reactive.asFlow
import org.slf4j.LoggerFactory
import org.springframework.data.mongodb.core.ReactiveMongoTemplate
import org.springframework.stereotype.Service
import java.time.Instant

@Service
class ChatMessageStreamService(
        private val reactiveMongoTemplate: ReactiveMongoTemplate
) {
    private val logger = LoggerFactory.getLogger(this.javaClass)

    fun watchChanges(id: String): Flow<ChatMessageVM> =
            reactiveMongoTemplate.changeStream(ChatMessage::class.java)
                    .watchCollection("chatMessage")
                    .resumeAt(Instant.now())
                    .listen()
                    .asFlow()
                    .filter { it.operationType == OperationType.INSERT }
                    .map { it.body!!.toView() }
                    .catch { logger.error("chat-message change stream error: ${it.message}") }
}