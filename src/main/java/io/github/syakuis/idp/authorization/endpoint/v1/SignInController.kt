package io.github.syakuis.idp.authorization.endpoint.v1

import org.springframework.http.MediaType
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping

@Controller
@RequestMapping("/sign-in")
class SignInController {

    @GetMapping(produces = [MediaType.TEXT_HTML_VALUE])
    fun signIn(): String {
        return "/authorization/sign-in"
    }
}