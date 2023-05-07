package talk.messageService.chatMessage

import kotlinx.coroutines.flow.*
import org.springframework.stereotype.Service
import talk.messageService.chatMessage.ChatMessage.Companion.chatMessage

@Service
class ChatMessageService(
        private val repository: ChatMessageRepository
) {
    val sender: MutableSharedFlow<ChatMessageVM> = MutableSharedFlow()

    fun stream(): Flow<ChatMessageVM> = sender

    suspend fun post(chatId: String, messages: Flow<ChatMessagePayload>) =
            messages
                    .map {
                        chatMessage {
                            this.chatId = chatId
                            this.chatParticipantId = it.chatParticipantId
                            this.content = it.content
                        }.run {
                            repository.save(this)
                        }
                    }
                    .onEach {
                        sender.emit(it.toView())
                    }.collect()

    fun latest(chatId: String): Flow<ChatMessageVM> =
            repository.findAllByChatId(chatId).map(ChatMessage::toView)

}