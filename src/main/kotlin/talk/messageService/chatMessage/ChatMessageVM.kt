package talk.messageService.chatMessage

data class ChatMessageVM(
        val chatParticipantId: String,
        val content: String
)

fun ChatMessage.toView() = ChatMessageVM(
        chatParticipantId = this.chatParticipantId,
        content = this.content
)