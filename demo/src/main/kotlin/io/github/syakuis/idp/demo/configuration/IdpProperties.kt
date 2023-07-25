package io.github.syakuis.idp.demo.configuration

import org.springframework.boot.context.properties.ConfigurationProperties

/**
 * @author Seok Kyun. Choi.
 * @since 2023-07-25
 */
@ConfigurationProperties(prefix = "idp")
data class IdpProperties(
    val host: String,
    val clientId: String,
    val clientSecret: String
)