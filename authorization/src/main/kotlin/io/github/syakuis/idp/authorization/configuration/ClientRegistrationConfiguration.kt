package io.github.syakuis.idp.authorization.configuration

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.oauth2.core.AuthorizationGrantType
import org.springframework.security.oauth2.core.ClientAuthenticationMethod
import org.springframework.security.oauth2.core.oidc.OidcScopes
import org.springframework.security.oauth2.server.authorization.client.InMemoryRegisteredClientRepository
import org.springframework.security.oauth2.server.authorization.client.RegisteredClient
import org.springframework.security.oauth2.server.authorization.client.RegisteredClientRepository
import org.springframework.security.oauth2.server.authorization.settings.ClientSettings
import org.springframework.security.oauth2.server.authorization.settings.TokenSettings
import java.time.Duration
import java.util.*

@Configuration(proxyBeanMethods = false)
class ClientRegistrationConfiguration {
    @Bean
    fun registeredClientRepository(): RegisteredClientRepository {
        val authorizationCode = RegisteredClient
            .withId(UUID.randomUUID().toString())
            .clientId("8ec2ed80-6af0-46fa-9d6b-7ca9c5c01ea2")
            .clientSecret("{noop}secret")
            .clientAuthenticationMethod(ClientAuthenticationMethod.CLIENT_SECRET_BASIC)
            .authorizationGrantType(AuthorizationGrantType.AUTHORIZATION_CODE)
            .authorizationGrantType(AuthorizationGrantType.REFRESH_TOKEN)
            .tokenSettings(TokenSettings.builder().accessTokenTimeToLive(Duration.ofHours(2)).build())
            .redirectUri("http://localhost:8082/oauth2/authorize")
            .redirectUri("http://localhost:8082/oauth2/success")
            .postLogoutRedirectUri("http://localhost:8082/oauth2/logout")
            .scope(OidcScopes.OPENID)
            .clientSettings(ClientSettings.builder().requireAuthorizationConsent(true).build())
            .build()

        val clientCredentials = RegisteredClient.withId(UUID.randomUUID().toString())
            .clientId("client-credentials")
            .clientSecret("{noop}secret2")
            .clientAuthenticationMethod(ClientAuthenticationMethod.CLIENT_SECRET_BASIC)
            .authorizationGrantType(AuthorizationGrantType.CLIENT_CREDENTIALS)
            .redirectUri("http://localhost:8082/oauth2/success")
            .postLogoutRedirectUri("http://localhost:8082/oauth2/logout")
            .scope(OidcScopes.OPENID)
            .clientSettings(ClientSettings.builder().requireAuthorizationConsent(true).build())
            .build()

        return InMemoryRegisteredClientRepository(authorizationCode, clientCredentials)
    }
}