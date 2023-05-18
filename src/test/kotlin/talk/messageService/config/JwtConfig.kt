package talk.messageService.config

import java.util.UUID
import javax.crypto.spec.SecretKeySpec


object JwtConfig {
    val secretKey = SecretKeySpec(UUID.randomUUID().toString().toByteArray(), "HmacSHA256")
}