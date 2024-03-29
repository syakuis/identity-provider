package io.github.syakuis.spring.web.controller.response

/**
 * @author Seok Kyun. Choi.
 * @since 2021-09-02
 */
object JsonRootName {
    fun <T> of(name: String, value: T): Map<String, T> {
        return mapOf(name to value)
    }
}