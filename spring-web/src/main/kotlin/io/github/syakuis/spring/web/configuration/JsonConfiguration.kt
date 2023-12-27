package io.github.syakuis.spring.web.configuration

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import io.github.syakuis.spring.web.configuration.support.SimpleObjectMapper
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration(proxyBeanMethods = false)
class JsonConfiguration {
    @ConditionalOnMissingBean(ObjectMapper::class)
    @Bean
    fun objectMapper() : ObjectMapper {
        return SimpleObjectMapper.of(jacksonObjectMapper())
    }
}