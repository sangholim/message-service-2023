package talk.messageService.chatMessage

import org.bson.types.ObjectId
import org.springframework.data.annotation.CreatedBy
import org.springframework.data.annotation.CreatedDate
import org.springframework.data.annotation.Id
import org.springframework.data.annotation.LastModifiedBy
import org.springframework.data.annotation.LastModifiedDate
import org.springframework.data.mongodb.core.mapping.Document
import java.time.Instant

/**
 * 채팅 메세지
 */
@Document
data class ChatMessage(
        /**
         * 채팅 메세지 id
         */
        @Id
        val id: ObjectId?,
        /**
         * 채팅 id
         */
        val chatId: String,
        /**
         * 채팅 참가자 id
         */
        val chatParticipantId: String,
        /**
         * 채팅 내용
         */
        val content: String,
        /**
         * 채팅 메세지 생성일
         */
        @CreatedDate
        val createdAt: Instant?,
        /**
         * 채팅 메세지 작성자
         */
        @CreatedBy
        val createdBy: String?
) {

    private constructor(builder: Builder) :
            this(builder.id, builder.chatId!!, builder.chatParticipantId!!, builder.content!!, builder.createdAt, builder.createdBy)

    companion object {
        inline fun chatMessage(block: Builder.() -> Unit) = Builder().apply(block).build()
    }

    class Builder {
        var id: ObjectId? = null
        var chatId: String? = null
        var chatParticipantId: String? = null
        var content: String? = null
        var createdAt: Instant? = null
        var createdBy: String? = null
        fun build(): ChatMessage {
            return ChatMessage(this)
        }
    }
}