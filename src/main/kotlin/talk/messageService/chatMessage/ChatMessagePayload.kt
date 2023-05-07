package talk.messageService.chatMessage

/**
 * 채팅 메세지 필드 데이터
 */
data class ChatMessagePayload(
        /**
         * 채팅 참가자 id
         */
        val chatParticipantId: String,
        /**
         * 채팅 내용
         */
        val content: String,
)
