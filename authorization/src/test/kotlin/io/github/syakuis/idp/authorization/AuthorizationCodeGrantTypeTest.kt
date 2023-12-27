package io.github.syakuis.idp.authorization

import io.kotest.core.spec.DisplayName
import io.kotest.core.spec.style.ShouldSpec
import io.kotest.matchers.string.shouldNotBeBlank
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.http.MediaType
import org.springframework.security.core.userdetails.User
import org.springframework.security.oauth2.core.AuthorizationGrantType
import org.springframework.security.oauth2.core.oidc.OidcScopes
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.httpBasic
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.post
import org.springframework.web.servlet.function.RequestPredicates.contentType
import org.springframework.web.util.UriComponentsBuilder

/**
 * @author Seok Kyun. Choi.
 * @since 2023-11-22
 */
@SpringBootTest
@AutoConfigureMockMvc
@DisplayName("Authorization Server 인증 방식")
class AuthorizationCodeGrantTypeTest : ShouldSpec() {
    @Autowired
    private lateinit var mvc: MockMvc

    private var code = ""

    private val clientId = "8ec2ed80-6af0-46fa-9d6b-7ca9c5c01ea2"
    private val clientSecret = "secret"
    private val user = User.withUsername("test").password("").build()

    init {
        context("OAuth2 Authentication Code 부여 방식") {
            should("인증된 사용자가 아니므로 로그인 페이지로 이동한다.") {
                mvc.post("/oauth2/authorize") {
                    contentType(MediaType.APPLICATION_FORM_URLENCODED)
                    param("response_type", "code")
                    param("client_id", "8ec2ed80-6af0-46fa-9d6b-7ca9c5c01ea2")
                    param("redirect_uri", "http://localhost:8082/oauth2/authorize")
                    param("scope", OidcScopes.OPENID)
                }.andExpect {
                    status { is3xxRedirection() }
                    redirectedUrl("http://localhost/sign-in")
                }
            }

            should("인증 코드 요청하고 redirectUrl로 인증 코드를 얻는 다.") {
                val result = mvc.post("/oauth2/authorize") {
                    contentType(MediaType.APPLICATION_FORM_URLENCODED)
                    param("response_type", "code")
                    param("client_id", clientId)
                    param("redirect_uri", "http://localhost:8082/oauth2/authorize")
                    param("scope", OidcScopes.OPENID)
                    with(user(user))
                }.andExpect {
                    status { is3xxRedirection() }
                    redirectedUrlPattern("http://localhost:8082/oauth2/authorize?code=*")
                }.andReturn()

                val redirectUrl = result.response.redirectedUrl ?: ""

                redirectUrl.shouldNotBeBlank()

                val uriComponents = UriComponentsBuilder.fromUriString(redirectUrl).build()
                code = uriComponents.queryParams.getFirst("code") ?: ""

                code.shouldNotBeBlank()
            }

            should("인증 코드로 인증 요청하고 액세스 토큰을 얻는 다.") {
                mvc.post("/oauth2/token") {
                    contentType(MediaType.APPLICATION_FORM_URLENCODED)
                    param("grant_type", AuthorizationGrantType.AUTHORIZATION_CODE.value)
                    param("code", code)
                    param("redirect_uri", "http://localhost:8082/oauth2/authorize")
                    with(httpBasic(clientId, clientSecret))
                    with(user(user))
                }.andExpect {
                    status { isOk() }

                    jsonPath("$.access_token") {
                        isNotEmpty()
                    }

                    jsonPath("$.refresh_token") {
                        isNotEmpty()
                    }
                }

            }
        }

    }
}