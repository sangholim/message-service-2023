package talk.messageService.chatMessage

import kotlinx.coroutines.flow.*
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service

@Service
class ChatMessageService {
    private val storage = mutableListOf<ChatMessageVM>()

    val sender: MutableSharedFlow<ChatMessageVM> = MutableSharedFlow()

    fun stream(): Flow<ChatMessageVM> = sender

    suspend fun post(messages: Flow<ChatMessageVM>) =
            messages
                    .onEach { sender.emit(it.copy()) }
                    .collect {
                        storage.add(it)
                    }

    fun latest(): Flow<ChatMessageVM> = flowOf(storage).filter { it.isNotEmpty() }.map {
        it.last()
    }
}