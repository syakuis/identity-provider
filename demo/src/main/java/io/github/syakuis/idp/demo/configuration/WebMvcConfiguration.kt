package io.github.syakuis.idp.demo.configuration;

import com.fasterxml.jackson.databind.ObjectMapper
import io.github.syakuis.idp.core.configuration.AbstractWebMvcConfiguration
import org.springframework.boot.autoconfigure.web.WebProperties
import org.springframework.context.annotation.Configuration

@Configuration(proxyBeanMethods = false)
class WebMvcConfiguration(
    objectMapper: ObjectMapper,
    webProperties: WebProperties
) : AbstractWebMvcConfiguration(objectMapper, webProperties)
