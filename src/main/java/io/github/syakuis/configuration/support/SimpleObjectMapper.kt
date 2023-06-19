package io.github.syakuis.configuration.support;

import com.fasterxml.jackson.annotation.JsonAutoDetect
import com.fasterxml.jackson.annotation.PropertyAccessor
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.datatype.jdk8.Jdk8Module
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateSerializer
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer
import com.fasterxml.jackson.module.paramnames.ParameterNamesModule
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.*

class SimpleObjectMapper {
    companion object {
        private const val DATE_FORMAT = "yyyy-MM-dd"
        private const val DATE_TIME_FORMAT = "yyyy-MM-dd'T'HH:mm:ss.SSSSSS"

        fun of(objectMapper: ObjectMapper): ObjectMapper {
            val module = JavaTimeModule()
            module.addSerializer(
                LocalDateTime::class.java,
                LocalDateTimeSerializer(DateTimeFormatter.ofPattern(DATE_TIME_FORMAT))
            )
            module.addSerializer(
                LocalDate::class.java,
                LocalDateSerializer(DateTimeFormatter.ofPattern(DATE_FORMAT))
            )

            val mapper = objectMapper.copy()
            mapper.setTimeZone(TimeZone.getTimeZone("UTC"))
            mapper.registerModule(ParameterNamesModule())
            mapper.registerModule(Jdk8Module())
            mapper.registerModule(module)
            mapper.setVisibility(PropertyAccessor.FIELD, JsonAutoDetect.Visibility.ANY)

            return mapper
        }
    }
}
