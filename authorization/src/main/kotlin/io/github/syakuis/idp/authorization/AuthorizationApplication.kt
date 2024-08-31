package io.github.syakuis.idp.authorization;

import io.github.syakuis.idp.authorization.configuration.AuthorizationServerProperties
import io.github.syakuis.idp.authorization.configuration.SecurityProperties
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.boot.runApplication

@SpringBootApplication
@EnableConfigurationProperties(value = [SecurityProperties::class, AuthorizationServerProperties::class])
class AuthorizationApplication

fun main(args: Array<String>) {
    runApplication<AuthorizationApplication>(*args)
}
