package io.github.syakuis.idp.configuration

import io.github.syakuis.idp.authorization.configuration.PasswordEncoderConfiguration
import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.matchers.shouldBe
import io.kotest.matchers.shouldNotBe
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.security.crypto.password.PasswordEncoder

@SpringBootTest(classes = [PasswordEncoderConfiguration::class])
class PasswordEncoderConfigurationTest : BehaviorSpec() {
    @Autowired
    private lateinit var passwordEncoder: PasswordEncoder

    init {
        Given("passwordEncoder 빈 생성함.") {

            Then("passwordEncoder 빈이 존재함.") {
                passwordEncoder shouldNotBe null
            }

            When("1234를 암호화한다.") {
                val encodedPassword = passwordEncoder.encode("1234")

                Then("1234 비밀번호는 암호화된 비밀번호와 일치한다. - $encodedPassword") {
                    passwordEncoder.matches("1234", encodedPassword) shouldBe true
                }

            }
        }
    }
}
