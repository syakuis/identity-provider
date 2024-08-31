package io.github.syakuis.idp.authorization.configuration

import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.boot.context.properties.bind.ConstructorBinding

/**
 * @author Seok Kyun. Choi.
 * @since 2024-08-31
 */
@ConfigurationProperties(prefix = "idp.security.oauth2.authorization-server")
data class AuthorizationServerProperties @ConstructorBinding constructor(
    val jwt: Jwt,
    val jks: Jks
) {
    data class Jwt(
        val issuerUri: String,
    )
    data class Jks(
        val location: String,
        val storePass: String,
        val alias: String,
    )
}