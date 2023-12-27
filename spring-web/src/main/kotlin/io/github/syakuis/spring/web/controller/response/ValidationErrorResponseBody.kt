package io.github.syakuis.spring.web.controller.response

import org.springframework.http.HttpStatus

/**
 * @author Seok Kyun. Choi.
 * @since 2021-05-21
 */
class ValidationErrorResponseBody(
    override val message: String,
    override val status: String,
    override val code: Int,
    resultStatus: ResultStatus? = null,
    httpStatus: HttpStatus,
    val details: List<Details>): ErrorResponseBody(message, status, code, resultStatus, httpStatus) {

    constructor(message: String? = null, resultStatus: ResultStatus, details: List<Details>): this(
        message = message?: resultStatus.message(),
        resultStatus.httpStatus().name,
        resultStatus.code(),
        resultStatus,
        resultStatus.httpStatus(),
        details)

    constructor(message: String? = null, httpStatus: HttpStatus, details: List<Details>): this(
        message = message?: httpStatus.reasonPhrase,
        httpStatus.name,
        httpStatus.hashCode(),
        null,
        httpStatus,
        details)

    class Details(val target: String, val message: String? = null, val code: String? = null)
}