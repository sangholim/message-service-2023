package talk.messageService.chatMessage

import kotlinx.coroutines.flow.Flow
import org.bson.types.ObjectId
import org.springframework.data.repository.kotlin.CoroutineCrudRepository

interface ChatMessageRepository: CoroutineCrudRepository<ChatMessage, ObjectId> {
    fun findAllByChatId(chatId: String): Flow<ChatMessage>
}