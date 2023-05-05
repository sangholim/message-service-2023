package talk.messageService.config

import io.rsocket.metadata.WellKnownMimeType
import org.springframework.boot.autoconfigure.rsocket.RSocketProperties
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.http.codec.cbor.Jackson2CborDecoder
import org.springframework.http.codec.cbor.Jackson2CborEncoder
import org.springframework.messaging.rsocket.RSocketRequester
import org.springframework.messaging.rsocket.RSocketStrategies
import org.springframework.util.MimeType
import java.net.URI

@Configuration
class WebSocketConfig(
        private val rSocketProperties: RSocketProperties
) {
    private val strategies = RSocketStrategies.builder()
            .encoders { it.add(Jackson2CborEncoder()) }
            .decoders { it.add(Jackson2CborDecoder()) }
            .build()

    @Bean
    fun rSocketRequester(): RSocketRequester =
            RSocketRequester.builder()
                    .rsocketStrategies(strategies)
                    .metadataMimeType(MimeType.valueOf(WellKnownMimeType.MESSAGE_RSOCKET_COMPOSITE_METADATA.string))
                    .websocket(
                            URI.create("ws://${rSocketProperties.server.address.hostAddress}:${rSocketProperties.server.port}/${rSocketProperties.server.mappingPath}")
                    )

}