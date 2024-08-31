package io.github.syakuis.idp.authorization.configuration

import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.boot.context.properties.bind.ConstructorBinding

/**
 * @author Seok Kyun. Choi.
 * @since 2024-08-31
 */
@ConfigurationProperties(prefix = "idp.security")
data class SecurityProperties @ConstructorBinding constructor(
    val loginUrl: String,
)