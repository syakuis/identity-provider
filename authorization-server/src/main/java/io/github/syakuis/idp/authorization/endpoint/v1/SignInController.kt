package io.github.syakuis.idp.authorization.endpoint.v1

import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.http.MediaType
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.GetMapping

@Controller
class SignInController {
    private val log: Logger = LoggerFactory.getLogger(SignInController::class.java)

    @GetMapping(path = ["/sign-in"], produces = [MediaType.TEXT_HTML_VALUE])
    fun signIn(): String {
        return "sign-in"
    }
}