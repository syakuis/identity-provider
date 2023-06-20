package io.github.syakuis.idp;

import com.fasterxml.jackson.databind.ObjectMapper
import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.matchers.shouldBe
import io.kotest.matchers.shouldNotBe
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.context.MessageSource
import org.springframework.context.support.MessageSourceAccessor

@SpringBootTest
internal class ApplicationTest : BehaviorSpec() {
    private val log = LoggerFactory.getLogger(this.javaClass)

    @Autowired
    private lateinit var messageSource: MessageSource

    @Autowired
    private lateinit var messageSourceAccessor: MessageSourceAccessor

    @Autowired
    private lateinit var objectMapper: ObjectMapper

    init {
        this.Given("spring bean 테스트") {
            Then("bean 생성되었다") {
                messageSource shouldNotBe null
                messageSourceAccessor shouldNotBe null
            }
            When("메시지 프로퍼티에서 author.name 값을 가져왔다") {
                val actual = messageSourceAccessor.getMessage("author.name")

                val expected = "Seokkyun. Choi.";
                Then("가져온 값은  $expected 이다.") {
                    expected shouldBe actual
                }

            }

            When("문자열 배열을 jackson으로 객체로 만든다") {
                val data = "[1,2,3,4]";
                val actual = objectMapper.readValue(data, ArrayList::class.java);

                Then("$data 배열과 일치한다.") {
                    listOf(1, 2, 3, 4) shouldBe actual
                }
            }
        }
    }
}
