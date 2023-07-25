package io.github.syakuis.idp.demo.endpoint.v1

import io.github.syakuis.idp.demo.configuration.IdpProperties
import org.springframework.http.MediaType
import org.springframework.util.LinkedMultiValueMap
import org.springframework.util.MultiValueMap
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.reactive.function.client.WebClient

/**
 * @author Seok Kyun. Choi.
 * @since 2023-07-25
 */
@RestController
class BffRestController(
    private val idpProperties: IdpProperties
) {
    @PostMapping("/oauth2/token")
    fun token(@RequestParam("code") code: String, @RequestParam("redirect_uri") redirectUri: String): String? {
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

        return client.post()
            .uri("/oauth2/token")
            .contentType(MediaType.APPLICATION_FORM_URLENCODED)
            .bodyValue(data) // POST 요청 본문에 데이터 추가
            .retrieve()
            .bodyToMono(String::class.java)
            .block()
    }
}