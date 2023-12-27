package io.github.syakuis.spring.web.controller.response

import org.springframework.http.HttpStatus

/**
 * @author Seok Kyun. Choi.
 * @since 2021-05-21
 */
interface ResultStatus {
    fun message(): String
    fun httpStatus(): HttpStatus
    fun name(): String {
        return httpStatus().name
    }

    fun code(): Int {
        return httpStatus().value()
    }

    override fun toString(): String
}