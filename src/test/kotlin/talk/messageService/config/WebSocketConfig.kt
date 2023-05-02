package talk.messageService.config

import org.springframework.boot.autoconfigure.rsocket.RSocketProperties
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.http.codec.cbor.Jackson2CborDecoder
import org.springframework.http.codec.cbor.Jackson2CborEncoder
import org.springframework.messaging.rsocket.RSocketRequester
import org.springframework.messaging.rsocket.RSocketStrategies
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
                    .websocket(
                            URI.create("ws://${rSocketProperties.server.address.hostAddress}:${rSocketProperties.server.port}/${rSocketProperties.server.mappingPath}")
                    )

}