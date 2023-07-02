package io.github.syakuis.idp.core.web

import jakarta.validation.ConstraintViolationException
import org.springframework.core.Ordered
import org.springframework.core.annotation.Order
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.MethodArgumentNotValidException
import org.springframework.web.bind.MissingRequestHeaderException
import org.springframework.web.bind.annotation.ControllerAdvice
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.context.request.WebRequest

/**
 * @author Seok Kyun. Choi.
 * @since 2022-07-13
 */
@Order(value = Ordered.HIGHEST_PRECEDENCE + 1)
@ControllerAdvice
class ValidationExceptionHandler {

    @ExceptionHandler(ConstraintViolationException::class)
    protected fun handleConstraintViolation(
        e: ConstraintViolationException,
        request: WebRequest
    ): ResponseEntity<Any> {
        val body = ValidationErrorResponseBody(
            resultStatus = DefaultResultStatus.METHOD_ARGUMENT_NOT_VALID,
            details = e.constraintViolations.stream().map {
                ValidationErrorResponseBody.Details(
                    it.propertyPath.toString(),
                    it.message,
                    it.messageTemplate
                )
            }.toList()
        )
        return ResponseEntity.status(body.httpStatus).body(body.wrapper())
    }

    @ExceptionHandler(MethodArgumentNotValidException::class)
    fun handleMethodArgumentNotValidException(
        e: MethodArgumentNotValidException,
        request: WebRequest
    ): ResponseEntity<Any> {
        val bindingResult = e.bindingResult
        val body = ValidationErrorResponseBody(
            resultStatus = DefaultResultStatus.METHOD_ARGUMENT_NOT_VALID,
            details = bindingResult.fieldErrors.stream().map {
                ValidationErrorResponseBody.Details(
                    it.field,
                    it.defaultMessage,
                    it.code
                )
            }.toList()
        )
        return ResponseEntity.badRequest().body(body.wrapper())
    }

    @ExceptionHandler(MissingRequestHeaderException::class)
    fun handleMissingRequestHeaderException(
        e: MissingRequestHeaderException,
        request: WebRequest
    ): ResponseEntity<Any> {
        val body = ErrorResponseBody(
            e.message,
            HttpStatus.BAD_REQUEST
        )
        return ResponseEntity.badRequest().body(body.wrapper())
    }
}