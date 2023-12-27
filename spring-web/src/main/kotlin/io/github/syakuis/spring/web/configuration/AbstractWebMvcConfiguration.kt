package io.github.syakuis.spring.web.configuration

import com.fasterxml.jackson.databind.ObjectMapper
import org.springframework.boot.autoconfigure.web.WebProperties
import org.springframework.context.annotation.Bean
import org.springframework.http.converter.HttpMessageConverter
import org.springframework.http.converter.StringHttpMessageConverter
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter
import org.springframework.web.servlet.LocaleResolver
import org.springframework.web.servlet.config.annotation.DelegatingWebMvcConfiguration
import org.springframework.web.servlet.config.annotation.InterceptorRegistry
import org.springframework.web.servlet.i18n.FixedLocaleResolver
import org.springframework.web.servlet.i18n.LocaleChangeInterceptor
import java.nio.charset.StandardCharsets

/**
 * @author Seok Kyun. Choi.
 * @since 2023-07-02
 */
abstract class AbstractWebMvcConfiguration(private val objectMapper: ObjectMapper, private val webProperties: WebProperties): DelegatingWebMvcConfiguration() {
    override fun configureMessageConverters(converters: MutableList<HttpMessageConverter<*>?>) {
        val stringConverter = StringHttpMessageConverter(StandardCharsets.UTF_8)
        converters.add(stringConverter)
        converters.add(MappingJackson2HttpMessageConverter(objectMapper))
        super.addDefaultHttpMessageConverters(converters)
    }

    override fun addInterceptors(registry: InterceptorRegistry) {
        val interceptor = LocaleChangeInterceptor()
        interceptor.paramName = "lang"
        registry.addInterceptor(interceptor)
    }

    @Bean
    override fun localeResolver(): LocaleResolver {
        return FixedLocaleResolver(webProperties.locale)
    }
}