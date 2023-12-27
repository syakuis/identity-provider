package io.github.syakuis.idp.authorization.adapter.web.controller.v1

import io.github.syakuis.idp.authorization.adapter.web.controller.v1.SignInController
import io.github.syakuis.idp.authorization.configuration.PasswordEncoderConfiguration
import io.github.syakuis.idp.authorization.configuration.WebSecurityConfiguration
import io.kotest.core.spec.style.ShouldSpec
import io.kotest.matchers.nulls.shouldNotBeNull
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Value
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.context.annotation.Import
import org.springframework.http.MediaType
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.get
import org.springframework.test.web.servlet.post
import org.springframework.web.servlet.function.RequestPredicates.contentType

@WebMvcTest(SignInController::class)
@Import(PasswordEncoderConfiguration::class, WebSecurityConfiguration::class)
class SignInControllerTest : ShouldSpec() {
    @Autowired
    private lateinit var mvc: MockMvc

    @Value("\${idp.security.login-form-url}")
    private lateinit var loginFormUrl: String

    init {
        context("로그인 화면을 출력하는 컨트롤러") {
            should("loginFormUrl 프로퍼티 값이 존재한다.") {
                loginFormUrl.shouldNotBeNull()
            }

            should("로그인 컨트롤러를 요청한다.") {
                mvc.get("/sign-in") {
                }.andExpect {
                    status {
                        isOk()
                    }
                }.andDo {
                    print()
                }
            }

            should("로그인 프로세스를 요청한다") {
                mvc.post("/sign-in") {
                    contentType(MediaType.APPLICATION_FORM_URLENCODED)
                    param("username", "test")
                    param("password", "1234")
                    with(csrf())
                }.andExpect {
                    status {
                        is3xxRedirection()
                        redirectedUrl("/")
                    }
                }
            }
        }
    }
}
