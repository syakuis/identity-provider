package io.github.syakuis.idp.authorization.endpoint.v1

import io.github.syakuis.idp.authorization.configuration.PasswordEncoderConfiguration
import io.github.syakuis.idp.authorization.configuration.WebSecurityConfiguration
import io.kotest.core.spec.style.BehaviorSpec
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.context.annotation.Import
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.get

@WebMvcTest(SignInController::class)
@Import(PasswordEncoderConfiguration::class, WebSecurityConfiguration::class)
class SignInControllerTest : BehaviorSpec() {
    @Autowired
    private lateinit var mvc: MockMvc

    init {
        this.Given("로그인 화면을 출력하는 컨트롤러") {
            When("로그인 컨트롤러를 요청한다.") {
                mvc.get("/sign-in") {
                }.andExpect {
                    status {
                        isOk()
                    }
                }.andDo {
                    print()
                }
            }

        }
    }
}
