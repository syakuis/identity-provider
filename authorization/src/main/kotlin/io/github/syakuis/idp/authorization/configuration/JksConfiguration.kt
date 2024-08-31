package io.github.syakuis.idp.authorization.configuration

import org.slf4j.LoggerFactory
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.core.io.support.PathMatchingResourcePatternResolver
import java.io.IOException
import java.security.KeyStore
import java.security.KeyStoreException
import java.security.NoSuchAlgorithmException
import java.security.UnrecoverableKeyException
import java.security.cert.CertificateException
import java.security.interfaces.RSAPrivateKey
import java.security.interfaces.RSAPublicKey

/**
 * @author Seok Kyun. Choi.
 * @since 2024-01-30
 */
@Configuration(proxyBeanMethods = false)
internal class JksConfiguration(
    private val authorizationServerProperties: AuthorizationServerProperties,
) {
    private val log = LoggerFactory.getLogger(this.javaClass)!!

    @Bean
    fun keyStore(): KeyStore {
        try {
            val keyStore = KeyStore.getInstance(KeyStore.getDefaultType())

            val resolver = PathMatchingResourcePatternResolver()
            val resourceAsStream = resolver.getResource(authorizationServerProperties.jks.location).inputStream

            keyStore.load(resourceAsStream, authorizationServerProperties.jks.storePass.toCharArray())
            return keyStore
        } catch (e: IOException) {
            log.error("Unable to load keystore: ${authorizationServerProperties.jks.location}", e)
        } catch (e: CertificateException) {
            log.error("Unable to load keystore: ${authorizationServerProperties.jks.location}", e)
        } catch (e: NoSuchAlgorithmException) {
            log.error("Unable to load keystore: ${authorizationServerProperties.jks.location}", e)
        } catch (e: KeyStoreException) {
            log.error("Unable to load keystore: ${authorizationServerProperties.jks.location}", e)
        }

        throw IllegalArgumentException("Unable to load keystore")
    }

    @Bean
    fun rsaPrivateKey(keyStore: KeyStore): RSAPrivateKey {
        try {
            val key = keyStore.getKey(authorizationServerProperties.jks.alias, authorizationServerProperties.jks.storePass.toCharArray())
            if (key is RSAPrivateKey) {
                return key
            }
        } catch (e: UnrecoverableKeyException) {
            log.error("Unable to load private key from keystore: ${authorizationServerProperties.jks.location}", e)
        } catch (e: NoSuchAlgorithmException) {
            log.error("Unable to load private key from keystore: ${authorizationServerProperties.jks.location}", e)
        } catch (e: KeyStoreException) {
            log.error("Unable to load private key from keystore: ${authorizationServerProperties.jks.location}", e)
        }

        throw IllegalArgumentException("Unable to load private key")
    }

    @Bean
    fun rsaPublicKey(keyStore: KeyStore): RSAPublicKey {
        try {
            val certificate = keyStore.getCertificate(authorizationServerProperties.jks.alias)
            val key = certificate.publicKey

            if (key is RSAPublicKey) {
                return key
            }
        } catch (e: KeyStoreException) {
            log.error("Unable to load private key from keystore: ${authorizationServerProperties.jks.location}", e)
        }

        throw IllegalArgumentException("Unable to load RSA public key")
    }
}
