package talk.messageService.config

import com.ninjasquad.springmockk.MockkBean
import com.ninjasquad.springmockk.MockkBeans
import org.springframework.boot.test.context.TestConfiguration
import org.springframework.context.annotation.Bean
import org.springframework.messaging.rsocket.RSocketStrategies
import org.springframework.messaging.rsocket.annotation.support.RSocketMessageHandler
import org.springframework.security.config.annotation.method.configuration.EnableReactiveMethodSecurity
import org.springframework.security.config.annotation.rsocket.EnableRSocketSecurity
import org.springframework.security.config.annotation.rsocket.RSocketSecurity
import org.springframework.security.messaging.handler.invocation.reactive.AuthenticationPrincipalArgumentResolver
import org.springframework.security.oauth2.jose.jws.MacAlgorithm
import org.springframework.security.oauth2.jwt.NimbusReactiveJwtDecoder
import org.springframework.security.oauth2.jwt.ReactiveJwtDecoder
import org.springframework.security.rsocket.core.PayloadSocketAcceptorInterceptor

@MockkBeans(
    value = [
        MockkBean(value = [SecurityConfig::class], relaxed = true)
    ]
)
@EnableRSocketSecurity
@EnableReactiveMethodSecurity
@TestConfiguration
class TestSecurityConfig(
) {
    private val securityConfigImpl = SecurityConfig()

    @Bean
    fun messageHandler(strategies: RSocketStrategies): RSocketMessageHandler =
        securityConfigImpl.messageHandler(strategies)

    @Bean
    fun rsocketInterceptor(rsocket: RSocketSecurity): PayloadSocketAcceptorInterceptor =
        securityConfigImpl.rsocketInterceptor(rsocket)

    @Bean
    fun jwtDecoder(): ReactiveJwtDecoder =
        NimbusReactiveJwtDecoder
            .withSecretKey(JwtConfig.secretKey)
            .macAlgorithm(MacAlgorithm.HS256)
            .build()
}