package io.github.syakuis.idp.demo.endpoint.v1

import com.fasterxml.jackson.databind.ObjectMapper
import io.github.syakuis.idp.demo.configuration.IdpProperties
import io.github.syakuis.idp.demo.endpoint.v1.model.OAuth2TokenDto
import org.slf4j.LoggerFactory
import org.springframework.http.MediaType
import org.springframework.util.LinkedMultiValueMap
import org.springframework.util.MultiValueMap
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.reactive.function.client.WebClient

/**
 * @author Seok Kyun. Choi.
 * @since 2023-07-25
 */
@RestController
@RequestMapping("/oauth2")
class OAuth2AuthorizationCodeRestController(
    private val idpProperties: IdpProperties,
    private val objectMapper: ObjectMapper
) {
    private val log = LoggerFactory.getLogger(OAuth2AuthorizationCodeRestController::class.java)

    @PostMapping("/token")
    fun token(@RequestParam("code") code: String, @RequestParam("redirect_uri") redirectUri: String): OAuth2TokenDto {
        val client = WebClient.builder()
            .baseUrl(idpProperties.host)
            .defaultHeaders {  headers ->
                headers.setBasicAuth(idpProperties.clientId, idpProperties.clientSecret)
            }
            .build()

        val data: MultiValueMap<String, String> = LinkedMultiValueMap<String, String>().apply {
            add("grant_type", "authorization_code")
            add("code", code)
            add("redirect_uri", redirectUri)
        }

        val body = client.post()
            .uri("/oauth2/token")
            .contentType(MediaType.APPLICATION_FORM_URLENCODED)
            .bodyValue(data) // POST 요청 본문에 데이터 추가
            .retrieve()
            .bodyToMono(String::class.java)
            .block()

        log.debug("{}", body)

        var token = objectMapper.readValue(body, OAuth2TokenDto::class.java)

        log.debug("{}", token)

        return token
    }
}