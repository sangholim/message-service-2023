package talk.messageService.chatMessage

import kotlinx.coroutines.flow.*
import org.springframework.stereotype.Service

@Service
class ChatMessageService {
    private val storage = mutableListOf<ChatMessageVM>()

    val sender: MutableSharedFlow<ChatMessageVM> = MutableSharedFlow()

    fun stream(): Flow<ChatMessageVM> = sender

    suspend fun post(messages: Flow<ChatMessageVM>) =
            messages
                    .let {
                        storage.addAll(it.toList())
                    }

    fun latest(): Flow<ChatMessageVM> = flowOf(storage.last()).catch { emptyFlow<ChatMessageVM>() }
}