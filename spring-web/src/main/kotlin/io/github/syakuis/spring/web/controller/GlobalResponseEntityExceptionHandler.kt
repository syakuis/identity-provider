package io.github.syakuis.spring.web.controller

import io.github.syakuis.spring.web.controller.response.ErrorResponseBody
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.core.Ordered
import org.springframework.core.annotation.Order
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpStatus
import org.springframework.http.HttpStatusCode
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.ControllerAdvice
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.context.request.WebRequest
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler

/**
 * @author Seok Kyun. Choi.
 * @since 2021-08-31
 */
@Order(Ordered.HIGHEST_PRECEDENCE + 2)
@ControllerAdvice
class GlobalResponseEntityExceptionHandler: ResponseEntityExceptionHandler() {
    private val log: Logger = LoggerFactory.getLogger(GlobalResponseEntityExceptionHandler::class.java)

    @ExceptionHandler(value = [IllegalArgumentException::class])
    protected fun handleIllegalArgument(e: IllegalArgumentException, request: WebRequest): ResponseEntity<Any> {
        log.error(e.message, e)
        val body = ErrorResponseBody(message = e.message, HttpStatus.BAD_GATEWAY)
        return ResponseEntity.status(body.httpStatus).body(body.wrapper())
    }

    override fun handleExceptionInternal(
        ex: java.lang.Exception,
        body: Any?,
        headers: HttpHeaders,
        statusCode: HttpStatusCode,
        request: WebRequest
    ): ResponseEntity<Any> {
        val responseEntity: ResponseEntity<Any> = super.handleExceptionInternal(ex, body, headers, statusCode, request)?: ResponseEntity(body, statusCode)
        return ResponseEntity(
            ErrorResponseBody(
                ex.message,
                HttpStatus.resolve(responseEntity.statusCode.value())?: HttpStatus.INTERNAL_SERVER_ERROR
            ).wrapper(), responseEntity.headers, responseEntity.statusCode
        )
    }
}