package io.github.syakuis.idp.demo.endpoint.v1

import io.github.syakuis.idp.demo.configuration.IdpProperties
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.util.StringUtils
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam

/**
 * @author Seok Kyun. Choi.
 * @since 2023-07-24
 */
@Controller
@RequestMapping("/oauth2")
class OAuth2AuthorizationCodeController(
    private val idpProperties: IdpProperties
) {
    private val log: Logger = LoggerFactory.getLogger(OAuth2AuthorizationCodeController::class.java)

    @Value("\${idp.oauth2.authorization-code.redirect-url}")
    private lateinit var redirectUrl: String

    @GetMapping(path = ["/authorize"])
    fun authorize(model: Model): String {
        model.addAttribute("clientId", idpProperties.clientId)
        model.addAttribute("host", idpProperties.host)
        model.addAttribute("redirectUri", redirectUrl)
        return "oauth2-authorize"
    }
    // todo bff 에서 처리하고 프론트에 대한 콜백 url 을 받아야 함.
    @GetMapping(path = ["/authorize-token"])
    fun token(@RequestParam params: Map<String, String>, model: Model,
                @RequestParam(name = "code", required = false) code: String): String {
        if (StringUtils.hasText(code)) {
            model.addAttribute("clientId", idpProperties.clientId)
            model.addAttribute("host", idpProperties.host)
            model.addAttribute("redirectUri", redirectUrl)
            model.addAttribute("code", code)

            return "oauth2-authorize-token"
        }

        if (params.isNotEmpty()) {
            params.forEach {
                model.addAttribute(it.key, it.value)
            }
        }
        return "oauth2-authorize-failure"
    }

    @GetMapping(path = ["/authorize-success"])
    fun success(): String {
        return "oauth2-authorize-success"
    }
}