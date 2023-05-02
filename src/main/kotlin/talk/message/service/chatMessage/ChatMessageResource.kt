package talk.message.service.chatMessage

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emitAll
import kotlinx.coroutines.flow.onStart
import org.springframework.messaging.handler.annotation.MessageMapping
import org.springframework.messaging.handler.annotation.Payload
import org.springframework.stereotype.Controller

@Controller
class ChatMessageResource(
        private val chatMessageService: ChatMessageService
) {
    /**
     * client 로부터 메세지를 받아서 저장
     */
    @MessageMapping(value = ["stream.chats"])
    suspend fun receive(@Payload inboundMessages: Flow<ChatMessageVM>) {
        chatMessageService.post(inboundMessages)
    }

    /**
     * client 로 메세지 전달
     */
    @MessageMapping(value = ["stream.chats"])
    fun send(): Flow<ChatMessageVM> = chatMessageService
            .stream()
            .onStart {
                emitAll(chatMessageService.latest())
            }
}