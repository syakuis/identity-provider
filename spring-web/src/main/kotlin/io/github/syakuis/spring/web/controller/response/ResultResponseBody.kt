package io.github.syakuis.spring.web.controller.response

/**
 * @author Seok Kyun. Choi.
 * @since 2021-05-21
 */
interface ResultResponseBody {
    val message: String
    val status: String
    val code: Int

    fun wrapper(): Map<String, ResultResponseBody> {
        return JsonRootName.of("error", this)
    }
}