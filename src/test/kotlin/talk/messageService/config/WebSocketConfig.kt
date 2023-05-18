package talk.messageService.config

import io.jsonwebtoken.Jwts
import io.jsonwebtoken.SignatureAlgorithm
import io.rsocket.metadata.WellKnownMimeType
import org.springframework.boot.autoconfigure.rsocket.RSocketProperties
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.http.codec.cbor.Jackson2CborDecoder
import org.springframework.http.codec.cbor.Jackson2CborEncoder
import org.springframework.messaging.rsocket.RSocketRequester
import org.springframework.messaging.rsocket.RSocketStrategies
import org.springframework.security.rsocket.metadata.BearerTokenAuthenticationEncoder
import org.springframework.security.rsocket.metadata.BearerTokenMetadata
import org.springframework.util.MimeTypeUtils.parseMimeType
import java.net.URI

@Configuration
class WebSocketConfig(
    private val rSocketProperties: RSocketProperties
) {
    private val strategies = RSocketStrategies.builder()
        .encoders {
            it.add(Jackson2CborEncoder())
            it.add(BearerTokenAuthenticationEncoder())
        }
        .decoders {
            it.add(Jackson2CborDecoder())
        }
        .build()

    @Bean
    fun rSocketRequester(): RSocketRequester {
        val token = Jwts.builder()
            .signWith(JwtConfig.secretKey, SignatureAlgorithm.HS256)
            .claim("sub", "user")
            .compact()

        return RSocketRequester.builder()
            .rsocketStrategies(strategies)
            .setupMetadata(
                BearerTokenMetadata(token),
                parseMimeType(WellKnownMimeType.MESSAGE_RSOCKET_AUTHENTICATION.string)
            )
            .websocket(
                URI.create("ws://${rSocketProperties.server.address.hostAddress}:${rSocketProperties.server.port}/${rSocketProperties.server.mappingPath}")
            )
    }
}