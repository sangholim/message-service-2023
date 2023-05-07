package talk.messageService.config

import io.kotest.core.config.AbstractProjectConfig
import io.kotest.core.extensions.Extension
import io.kotest.extensions.spring.SpringExtension
import org.testcontainers.containers.MongoDBContainer
import org.testcontainers.utility.DockerImageName

object ExtensionConfig: AbstractProjectConfig() {
    val mongoDBContainer = MongoDBContainer(DockerImageName.parse("mongo:4.0.10"))
    init {
        mongoDBContainer.start()
        System.setProperty("spring.data.mongodb.url", mongoDBContainer.replicaSetUrl)
    }
    override fun extensions(): List<Extension> {
        return super.extensions().plus(SpringExtension)
    }
}