package io.github.syakuis.idp.authorization.configuration

import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean
import org.springframework.context.ApplicationEventPublisher
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.core.annotation.Order
import org.springframework.security.authentication.AuthenticationEventPublisher
import org.springframework.security.authentication.DefaultAuthenticationEventPublisher
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.config.annotation.web.invoke
import org.springframework.security.config.http.SessionCreationPolicy
import org.springframework.security.core.userdetails.User
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.security.provisioning.InMemoryUserDetailsManager
import org.springframework.security.web.SecurityFilterChain
import org.springframework.security.web.savedrequest.HttpSessionRequestCache


@Configuration(proxyBeanMethods = false)
@EnableWebSecurity
class WebSecurityConfiguration(private val passwordEncoder: PasswordEncoder) {
    /**
     * 사용자 계정 정보
     */
    @Bean
    @ConditionalOnMissingBean(UserDetailsService::class)
    fun userDetailsService(): UserDetailsService {
        return InMemoryUserDetailsManager(User.withUsername("test").password(passwordEncoder.encode("1234")).build())
    }

    /**
     * 인증 이벤트
     */
    @Bean
    @ConditionalOnMissingBean(AuthenticationEventPublisher::class)
    fun authenticationEventPublisher(delegate: ApplicationEventPublisher): DefaultAuthenticationEventPublisher {
        return DefaultAuthenticationEventPublisher(delegate)
    }

    @Order(2)
    @Bean
    fun forLoginFilterChain(http: HttpSecurity): SecurityFilterChain {
        val httpRequestCache = HttpSessionRequestCache()
        httpRequestCache.setMatchingRequestParameterName("continue")

        http {
            sessionManagement {
                sessionCreationPolicy = SessionCreationPolicy.NEVER
            }

            requestCache {
                requestCache = httpRequestCache
            }

            authorizeRequests {
                authorize(anyRequest, authenticated)
            }

            formLogin {
                loginPage = "/sign-in"
                loginProcessingUrl = "/sign-in"
                defaultSuccessUrl("/profiles/me", false)
                permitAll()
            }
        }

        return http.build()
    }
}