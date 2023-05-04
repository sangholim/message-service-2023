package talk.messageService.chatMessage

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emitAll
import kotlinx.coroutines.flow.onStart
import org.slf4j.LoggerFactory
import org.springframework.messaging.handler.annotation.DestinationVariable
import org.springframework.messaging.handler.annotation.MessageMapping
import org.springframework.messaging.handler.annotation.Payload
import org.springframework.stereotype.Controller

@Controller
class ChatMessageResource(
        private val chatMessageService: ChatMessageService
) {
    private val logger = LoggerFactory.getLogger(this.javaClass)

    /**
     * client 로부터 메세지를 받아서 저장
     */
    @MessageMapping(value = ["stream.chats.{id}.message"])
    suspend fun receive(@DestinationVariable id: String, @Payload inboundMessages: Flow<ChatMessageVM>) {
        logger.debug("receive message: $id")
        chatMessageService.post(inboundMessages)
    }

    /**
     * client 로 메세지 전달
     */
    @MessageMapping(value = ["stream.chats.{id}.message"])
    fun send(@DestinationVariable id: String): Flow<ChatMessageVM> = chatMessageService
            .stream()
            .onStart {
                logger.debug("send message: $id")
                emitAll(chatMessageService.latest())
            }
}