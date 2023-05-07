package talk.messageService.chatMessage

import org.bson.types.ObjectId
import org.springframework.data.repository.kotlin.CoroutineCrudRepository

interface ChatMessageRepository: CoroutineCrudRepository<ChatMessage, ObjectId>