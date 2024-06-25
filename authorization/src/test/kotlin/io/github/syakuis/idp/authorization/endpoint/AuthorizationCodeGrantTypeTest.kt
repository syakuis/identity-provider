package io.github.syakuis.idp.authorization.endpoint

import com.jayway.jsonpath.JsonPath
import io.kotest.core.spec.style.ShouldSpec
import io.kotest.matchers.collections.shouldHaveSize
import io.kotest.matchers.nulls.shouldNotBeNull
import io.kotest.matchers.shouldBe
import io.kotest.matchers.string.shouldNotBeBlank
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.http.MediaType
import org.springframework.security.core.userdetails.User
import org.springframework.security.oauth2.core.AuthorizationGrantType
import org.springframework.security.oauth2.core.oidc.OidcScopes
import org.springframework.security.oauth2.jwt.Jwt
import org.springframework.security.oauth2.jwt.JwtDecoder
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.httpBasic
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.post
import org.springframework.web.servlet.function.RequestPredicates.contentType
import org.springframework.web.util.UriComponentsBuilder

/**
 * @author Seok Kyun. Choi.
 * @since 2023-11-22
 *
 * http://localhost:8080/oauth2/authorize?response_type=code&client_id=client-id&redirect_uri=http://localhost:8080&scope=profile&state=abc123
 */
@SpringBootTest
@AutoConfigureMockMvc
class AuthorizationCodeGrantTypeTest : ShouldSpec() {
    @Autowired
    private lateinit var mvc: MockMvc
    @Autowired
    private lateinit var jwtDecoder: JwtDecoder

    private var host = "http://localhost:8082";

    private var code = ""
    private var accessToken = ""

    private val clientId = "8ec2ed80-6af0-46fa-9d6b-7ca9c5c01ea2"
    private val clientSecret = "secret"
    private val user = User.withUsername("test").password("").build()

    init {
        this.context("인증 코드 부여 방식 엔드포인트 테스트") {
            should("인증된 사용자가 아니면 로그인 페이지로 이동한다.") {
                mvc.post("/oauth2/authorize") {
                    contentType(MediaType.APPLICATION_FORM_URLENCODED)
                    param("response_type", "code")
                    param("client_id", "8ec2ed80-6af0-46fa-9d6b-7ca9c5c01ea2")
                    param("redirect_uri", "$host/oauth2/authorize")
                    param("scope", OidcScopes.OPENID)
                }.andExpect {
                    status { is3xxRedirection() }
                    redirectedUrl("http://localhost/sign-in")
                }
            }

            should("인증된 사용자면 redirectUrl로 인증 코드를 전달한다.") {
                val result = mvc.post("/oauth2/authorize") {
                    contentType(MediaType.APPLICATION_FORM_URLENCODED)
                    param("response_type", "code")
                    param("client_id", clientId)
                    param("redirect_uri", "$host/oauth2/authorize")
                    param("scope", OidcScopes.OPENID)
                    with(user(user))
                }.andExpect {
                    status { is3xxRedirection() }
                    redirectedUrlPattern("$host/oauth2/authorize?code=*")
                }.andReturn()

                val redirectUrl = result.response.redirectedUrl ?: ""

                redirectUrl.shouldNotBeBlank()

                val uriComponents = UriComponentsBuilder.fromUriString(redirectUrl).build()
                code = uriComponents.queryParams.getFirst("code") ?: ""

                code.shouldNotBeBlank()
            }

            should("인증 코드로 인증 토큰을 얻는 다.") {
                val result = mvc.post("/oauth2/token") {
                    contentType(MediaType.APPLICATION_FORM_URLENCODED)
                    param("grant_type", AuthorizationGrantType.AUTHORIZATION_CODE.value)
                    param("code", code)
                    param("redirect_uri", "$host/oauth2/authorize")
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
                }.andReturn()

                val accessToken = JsonPath.read<String>(result.response.contentAsString, "$.access_token")

                accessToken.split(".").shouldHaveSize(3)

                val jwt: Jwt = jwtDecoder.decode(accessToken)

                jwt.shouldNotBeNull()

                jwt.claims["custom-claim"].shouldBe("custom-value")
            }
        }
    }
}