package talk.messageService.jwt

import org.springframework.boot.context.properties.ConfigurationProperties


@ConfigurationProperties(prefix = "jwt")
data class JwtProperties(
    val issuerUri: String
)