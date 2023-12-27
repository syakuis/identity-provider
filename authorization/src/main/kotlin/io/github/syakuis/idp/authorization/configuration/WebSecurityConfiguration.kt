package io.github.syakuis.idp.authorization.configuration

import org.springframework.beans.factory.annotation.Value
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
import org.springframework.security.core.userdetails.User
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.security.provisioning.InMemoryUserDetailsManager
import org.springframework.security.web.SecurityFilterChain


@Configuration(proxyBeanMethods = false)
@EnableWebSecurity
class WebSecurityConfiguration(private val passwordEncoder: PasswordEncoder) {
    @Value("\${idp.security.login-form-url}")
    private lateinit var loginFormUrl: String

    @Bean
    @ConditionalOnMissingBean(UserDetailsService::class)
    fun userDetailsService(): UserDetailsService {
        return InMemoryUserDetailsManager(User.withUsername("test")
            .password(passwordEncoder.encode("1234"))
            .roles("USER")
            .build())
    }

    @Bean
    @ConditionalOnMissingBean(AuthenticationEventPublisher::class)
    fun authenticationEventPublisher(delegate: ApplicationEventPublisher): DefaultAuthenticationEventPublisher {
        return DefaultAuthenticationEventPublisher(delegate)
    }

    @Order(2)
    @Bean
    fun loginFilterChain(http: HttpSecurity): SecurityFilterChain {
        http {
            authorizeRequests {
                authorize(anyRequest, authenticated)
            }

            formLogin {
                loginPage = loginFormUrl
                permitAll()
            }

        }

        return http.build()
    }
}