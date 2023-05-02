package talk.messageService

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class TalkMessageServiceApplication

fun main(args: Array<String>) {
	runApplication<TalkMessageServiceApplication>(*args)
}
