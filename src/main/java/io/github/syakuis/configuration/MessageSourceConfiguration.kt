package io.github.syakuis.configuration;

import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean
import org.springframework.context.MessageSource
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.support.MessageSourceAccessor
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean
import java.util.*

@Configuration(proxyBeanMethods = false)
class MessageSourceConfiguration(private val messageSource: MessageSource) {
    @ConditionalOnMissingBean
    @Bean
    fun messageSourceAccessor(): MessageSourceAccessor {
        return MessageSourceAccessor(messageSource, Locale.getDefault())
    }

    @ConditionalOnMissingBean
    @Bean
    fun validator(): LocalValidatorFactoryBean {
        val bean = LocalValidatorFactoryBean()
        bean.setValidationMessageSource(messageSource)
        return bean
    }
}
