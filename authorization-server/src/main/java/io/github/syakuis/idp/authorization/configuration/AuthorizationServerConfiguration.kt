package io.github.syakuis.idp.authorization.configuration

import com.nimbusds.jose.jwk.JWKSet
import com.nimbusds.jose.jwk.RSAKey
import com.nimbusds.jose.jwk.source.ImmutableJWKSet
import com.nimbusds.jose.jwk.source.JWKSource
import com.nimbusds.jose.proc.SecurityContext
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.core.annotation.Order
import org.springframework.http.MediaType
import org.springframework.security.config.Customizer
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.oauth2.core.AuthorizationGrantType
import org.springframework.security.oauth2.core.ClientAuthenticationMethod
import org.springframework.security.oauth2.core.oidc.OidcScopes
import org.springframework.security.oauth2.jwt.JwtDecoder
import org.springframework.security.oauth2.server.authorization.client.InMemoryRegisteredClientRepository
import org.springframework.security.oauth2.server.authorization.client.RegisteredClient
import org.springframework.security.oauth2.server.authorization.client.RegisteredClientRepository
import org.springframework.security.oauth2.server.authorization.config.annotation.web.configuration.OAuth2AuthorizationServerConfiguration
import org.springframework.security.oauth2.server.authorization.config.annotation.web.configurers.OAuth2AuthorizationServerConfigurer
import org.springframework.security.oauth2.server.authorization.settings.AuthorizationServerSettings
import org.springframework.security.oauth2.server.authorization.settings.ClientSettings
import org.springframework.security.oauth2.server.authorization.settings.TokenSettings
import org.springframework.security.web.SecurityFilterChain
import org.springframework.security.web.authentication.LoginUrlAuthenticationEntryPoint
import org.springframework.security.web.util.matcher.MediaTypeRequestMatcher
import java.security.KeyPair
import java.security.KeyPairGenerator
import java.security.interfaces.RSAPrivateKey
import java.security.interfaces.RSAPublicKey
import java.time.Duration
import java.util.*

/**
 * http://localhost:8080/oauth2/authorize?response_type=code&client_id=client-id&redirect_uri=http://localhost:8080&scope=profile&state=abc123
 * @author Seok Kyun. Choi.
 * @since 2023-06-24
 */
@Configuration
@EnableWebSecurity
class AuthorizationServerConfiguration {
    @Value("\${idp.security.login-form-url}")
    private lateinit var loginFormUrl: String
    @Order(1)
    @Bean
    fun authorizationServerSecurityFilterChain(http: HttpSecurity): SecurityFilterChain {
        OAuth2AuthorizationServerConfiguration.applyDefaultSecurity(http)
        http.getConfigurer(OAuth2AuthorizationServerConfigurer::class.java).oidc(Customizer.withDefaults())

        http.exceptionHandling{
            it.defaultAuthenticationEntryPointFor(LoginUrlAuthenticationEntryPoint(loginFormUrl)
                , MediaTypeRequestMatcher(MediaType.TEXT_HTML))
        }

        return http.build()
    }

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
            .redirectUri("http://localhost:8081/oauth2/authorize-token")
            .redirectUri("http://localhost:8081/oauth2/authorize-success")
            .postLogoutRedirectUri("http://localhost:8080")
            .scope(OidcScopes.OPENID)
            .scope(OidcScopes.PROFILE)
            .clientSettings(ClientSettings.builder().requireAuthorizationConsent(true).build())
            .build()

        val clientCredentials = RegisteredClient.withId(UUID.randomUUID().toString())
            .clientId("client-credentials")
            .clientSecret("{noop}secret2")
            .clientAuthenticationMethod(ClientAuthenticationMethod.CLIENT_SECRET_BASIC)
            .authorizationGrantType(AuthorizationGrantType.CLIENT_CREDENTIALS)
            .redirectUri("http://localhost:8081/oauth2/success")
            .postLogoutRedirectUri("http://localhost:8080")
            .scope(OidcScopes.OPENID)
            .clientSettings(ClientSettings.builder().
            requireAuthorizationConsent(true)
                .build())
            .build()

        return InMemoryRegisteredClientRepository(authorizationCode, clientCredentials)
    }

    @Bean
    fun jwkSource(): JWKSource<SecurityContext> {
        val keyPair =  generateRsaKey()
        val publicKey = keyPair.public as RSAPublicKey
        val privateKey = keyPair.private as RSAPrivateKey
        val rsaKey = RSAKey.Builder(publicKey)
            .privateKey(privateKey).keyID(UUID.randomUUID().toString()).build()


        return ImmutableJWKSet(JWKSet(rsaKey))
    }

    private fun generateRsaKey(): KeyPair {
        val keyPair: KeyPair

        try {
            val keyPairGenerator = KeyPairGenerator.getInstance("RSA")
            keyPairGenerator.initialize(2048)
            keyPair = keyPairGenerator.generateKeyPair()
        } catch (e: Exception) {
            throw IllegalStateException(e)
        }

        return keyPair
    }

    @Bean
    fun jwtDecoder(jwkSource: JWKSource<SecurityContext>): JwtDecoder {
        return OAuth2AuthorizationServerConfiguration.jwtDecoder(jwkSource)
    }

    @Bean
    fun authorizationServerSettings(): AuthorizationServerSettings {
        return AuthorizationServerSettings.builder().build()
    }
}