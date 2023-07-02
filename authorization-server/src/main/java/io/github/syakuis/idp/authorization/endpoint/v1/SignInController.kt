package io.github.syakuis.idp.authorization.endpoint.v1

import org.springframework.http.MediaType
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.GetMapping

@Controller
class SignInController {

    @GetMapping(path = ["/sign-in"], produces = [MediaType.TEXT_HTML_VALUE])
    fun signIn(): String {
        return "sign-in"
    }
}