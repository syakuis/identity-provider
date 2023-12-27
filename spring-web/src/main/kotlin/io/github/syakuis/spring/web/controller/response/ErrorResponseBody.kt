package io.github.syakuis.spring.web.controller.response

import org.springframework.http.HttpStatus

/**
 * @author Seok Kyun. Choi.
 * @since 2021-05-21
 */
open class ErrorResponseBody(
    override val message: String,
    override val status: String,
    override val code: Int,
    val resultStatus: ResultStatus? = null,
    val httpStatus: HttpStatus
    ): ResultResponseBody {
    constructor(message: String? = null, resultStatus: ResultStatus): this(
        message = message?: resultStatus.message(),
        resultStatus.httpStatus().name,
        resultStatus.code(),
        resultStatus,
        resultStatus.httpStatus())

    constructor(message: String? = null, httpStatus: HttpStatus): this(
        message = message?: httpStatus.reasonPhrase,
        httpStatus.name,
        httpStatus.hashCode(),
        null,
        httpStatus
    )
}