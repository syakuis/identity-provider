package io.github.syakuis.idp.demo.endpoint.v1.model

import wiremock.com.fasterxml.jackson.annotation.JsonProperty

/**
 * @author Seok Kyun. Choi.
 * @since 2023-08-09
 */
data class OAuth2TokenDto(
    @JsonProperty("access_token")
    var accessToken: String?,
    @JsonProperty("refresh_token")
    var refreshToken: String?,
    @JsonProperty("token_type")
    var tokenType: String?,
    @JsonProperty("expires_in")
    var expiresIn: Int?
)