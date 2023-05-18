package talk.messageService.chatMessage

import kotlinx.coroutines.flow.*
import org.springframework.messaging.handler.annotation.DestinationVariable
import org.springframework.messaging.handler.annotation.MessageMapping
import org.springframework.messaging.handler.annotation.Payload
import org.springframework.stereotype.Controller

@Controller
class ChatMessageResource(
        private val chatMessageService: ChatMessageService,
        private val chatMessageStreamService: ChatMessageStreamService
) {
    /**
     * client 로부터 메세지를 받아서 저장
     */
    @MessageMapping(value = ["stream.chats.{id}.message"])
    suspend fun receive(@DestinationVariable id: String, @Payload inboundMessages: Flow<ChatMessagePayload>) {
        chatMessageService.post(id, inboundMessages)
    }

    /**
     * client 로 메세지 전달
     */
    @MessageMapping(value = ["stream.chats.{id}.message"])
    fun send(@DestinationVariable id: String): Flow<ChatMessageVM> =
            chatMessageStreamService.watchChanges(id)
}