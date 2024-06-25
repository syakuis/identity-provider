package io.github.syakuis.idp.authorization.configuration.custom

import org.springframework.security.oauth2.jwt.JwtClaimsSet
import org.springframework.security.oauth2.server.authorization.token.JwtEncodingContext
import org.springframework.security.oauth2.server.authorization.token.OAuth2TokenCustomizer

class CustomJwtTokenCustomizer : OAuth2TokenCustomizer<JwtEncodingContext> {

    override fun customize(context: JwtEncodingContext) {
        val claims: JwtClaimsSet.Builder = context.claims

        claims.claim("custom-claim", "custom-value")
    }
}