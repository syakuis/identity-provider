package io.github.syakuis.idp.authorization.configuration

import io.github.syakuis.idp.configuration.PasswordEncoderConfiguration
import io.kotest.core.spec.style.BehaviorSpec
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.context.annotation.Import
import org.springframework.http.MediaType
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.post
import org.springframework.web.servlet.function.RequestPredicates.contentType

@WebMvcTest
@Import(PasswordEncoderConfiguration::class, WebSecurityConfiguration::class)
class SignInProcessTest : BehaviorSpec() {
    @Autowired
    private lateinit var mvc: MockMvc

    init {
        this.Given("로그인하여 인증받는 다") {
            /*When("mocking 로그인 프로세스를 요청한다") {
                mvc.perform(
                    formLogin().user("test").password("1234")
                )
                    .andDo { MockMvcResultHandlers.print() }
                    .andExpect(status().is3xxRedirection())
                    .andExpect(redirectedUrl("/"))
            }*/

            When("로그인 프로세스를 요청한다") {
                val actual = mvc.post("/sign-in") {
                    param("username", "test")
                    param("password", "1234")
                    contentType(MediaType.APPLICATION_FORM_URLENCODED)
                }.andDo {
                    print()
                }

                Then("로그인하고 홈으로 리다이렉트한다.") {
                    actual.andExpect {
                        status {
                            is3xxRedirection()
                            redirectedUrl("/")
                        }
                    }
                }
            }

        }
    }
}
