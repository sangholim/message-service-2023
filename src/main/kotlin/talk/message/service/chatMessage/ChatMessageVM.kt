package talk.message.service.chatMessage

data class ChatMessageVM(
        val name: String,
        val text: String
)


fun ChatMessageVM.transform(): String = "$name: $text 입니다."