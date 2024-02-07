package io.github.syakuis.idp.authorization.configuration

import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Value
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
    @Value("\${idp.security.oauth2.authorizationserver.jks.keystore}")
    private val keystore: String,
    @Value("\${idp.security.oauth2.authorizationserver.jks.storepass}")
    private val storepass: String,
    @Value("\${idp.security.oauth2.authorizationserver.jks.alias}")
    private val alias: String
) {
    private val log = LoggerFactory.getLogger(this.javaClass)!!

    @Bean
    fun keyStore(): KeyStore {
        try {
            val keyStore = KeyStore.getInstance(KeyStore.getDefaultType())

            val resolver = PathMatchingResourcePatternResolver()
            val resourceAsStream = resolver.getResource(keystore).inputStream

            keyStore.load(resourceAsStream, storepass.toCharArray())
            return keyStore
        } catch (e: IOException) {
            log.error("Unable to load keystore: {}", keystore, e)
        } catch (e: CertificateException) {
            log.error("Unable to load keystore: {}", keystore, e)
        } catch (e: NoSuchAlgorithmException) {
            log.error("Unable to load keystore: {}", keystore, e)
        } catch (e: KeyStoreException) {
            log.error("Unable to load keystore: {}", keystore, e)
        }

        throw IllegalArgumentException("Unable to load keystore")
    }

    @Bean
    fun rsaPrivateKey(keyStore: KeyStore): RSAPrivateKey {
        try {
            val key = keyStore.getKey(alias, storepass.toCharArray())
            if (key is RSAPrivateKey) {
                return key
            }
        } catch (e: UnrecoverableKeyException) {
            log.error("Unable to load private key from keystore: {}", keystore, e)
        } catch (e: NoSuchAlgorithmException) {
            log.error("Unable to load private key from keystore: {}", keystore, e)
        } catch (e: KeyStoreException) {
            log.error("Unable to load private key from keystore: {}", keystore, e)
        }

        throw IllegalArgumentException("Unable to load private key")
    }

    @Bean
    fun rsaPublicKey(keyStore: KeyStore): RSAPublicKey {
        try {
            val certificate = keyStore.getCertificate(alias)
            val publicKey = certificate.publicKey

            if (publicKey is RSAPublicKey) {
                return publicKey
            }
        } catch (e: KeyStoreException) {
            log.error("Unable to load private key from keystore: {}", keystore, e)
        }

        throw IllegalArgumentException("Unable to load RSA public key")
    }
}
