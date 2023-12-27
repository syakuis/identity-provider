package io.github.syakuis.spring.web.controller.response

import org.springframework.http.HttpStatus

/**
 * @author Seok Kyun. Choi.
 * @since 2023-07-02
 */
enum class DefaultResultStatus(private val httpStatus: HttpStatus, private val message: String, private val code: Int) :
    ResultStatus {
    METHOD_ARGUMENT_NOT_VALID(HttpStatus.BAD_REQUEST, "입력한 값이 유효하지 않습니다.", 400_001);

    override fun message(): String {
        return message
    }

    override fun httpStatus(): HttpStatus {
        return httpStatus
    }

    override fun code(): Int {
        return code
    }
}