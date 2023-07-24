package io.github.syakuis.idp.demo.endpoint.v1

import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam

/**
 * @author Seok Kyun. Choi.
 * @since 2023-07-24
 */
@Controller
@RequestMapping("/oauth2")
class OAuth2Controller {
    private val log: Logger = LoggerFactory.getLogger(OAuth2Controller::class.java)

    @GetMapping(path = ["/authorize"])
    fun render(model: Model): String {
        // todo properties
        model.addAttribute("clientId", "8ec2ed80-6af0-46fa-9d6b-7ca9c5c01ea2")
        model.addAttribute("host", "http://localhost:8080")
        model.addAttribute("redirectUri", "http://localhost:8081/oauth2/success")
        return "oauth2-authorize"
    }
    @GetMapping(path = ["/success"])
    fun success(@RequestParam params: Map<String, String>, model: Model): String {
        params.forEach {
            model.addAttribute(it.key, it.value)
        }

        return "oauth2-success"
    }
}