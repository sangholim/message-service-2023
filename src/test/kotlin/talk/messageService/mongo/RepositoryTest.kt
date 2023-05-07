package talk.messageService.mongo

import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest
import org.springframework.context.annotation.Import
import talk.messageService.config.MongoConfig

@DataMongoTest
@Import(value = [MongoConfig::class])
annotation class RepositoryTest
