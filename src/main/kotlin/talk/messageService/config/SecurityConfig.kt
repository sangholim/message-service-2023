package talk.messageService.config

import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.messaging.rsocket.RSocketStrategies
import org.springframework.messaging.rsocket.annotation.support.RSocketMessageHandler
import org.springframework.security.config.Customizer.withDefaults
import org.springframework.security.config.annotation.method.configuration.EnableReactiveMethodSecurity
import org.springframework.security.config.annotation.rsocket.EnableRSocketSecurity
import org.springframework.security.config.annotation.rsocket.RSocketSecurity
import org.springframework.security.messaging.handler.invocation.reactive.AuthenticationPrincipalArgumentResolver
import org.springframework.security.oauth2.jwt.ReactiveJwtDecoder
import org.springframework.security.oauth2.jwt.ReactiveJwtDecoders
import org.springframework.security.rsocket.core.PayloadSocketAcceptorInterceptor
import talk.messageService.jwt.JwtProperties

@Configuration
@EnableRSocketSecurity
@EnableReactiveMethodSecurity
@EnableConfigurationProperties(JwtProperties::class)
class SecurityConfig {
    @Bean
    fun messageHandler(strategies: RSocketStrategies): RSocketMessageHandler {
        val handler = RSocketMessageHandler()
        handler.argumentResolverConfigurer.addCustomResolver(AuthenticationPrincipalArgumentResolver())
        handler.rSocketStrategies = strategies
        return handler
    }

    @Bean
    fun rsocketInterceptor(rsocket: RSocketSecurity): PayloadSocketAcceptorInterceptor {
        rsocket
            .authorizePayload { authorize ->
                authorize
                    .anyRequest().authenticated()
                    .anyExchange().permitAll()
            }
            .jwt(withDefaults())
        return rsocket.build()
    }

    @Bean
    fun jwtDecoder(properties: JwtProperties): ReactiveJwtDecoder =
        ReactiveJwtDecoders.fromIssuerLocation(properties.issuerUri)
}