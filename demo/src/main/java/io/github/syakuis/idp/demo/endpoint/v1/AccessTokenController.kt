package io.github.syakuis.idp.demo.endpoint.v1

import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping

@Controller
@RequestMapping("/access-tokens")
class AccessTokenController {
    @GetMapping
    fun render(model: Model): String {
        // todo properties
        model.addAttribute("clientId", "client-id")
        model.addAttribute("host", "http://localhost:8080")
        model.addAttribute("redirectUri", "http://localhost:8081/access-tokens")
        return "access-token"
    }
}