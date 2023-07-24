package io.github.syakuis.idp

import io.kotest.core.config.AbstractProjectConfig
import io.kotest.extensions.spring.SpringExtension

/**
 * @author Seok Kyun. Choi.
 * @since 2021-05-30
 */
class ProjectConfig : AbstractProjectConfig() {
    override fun beforeAll() {
        System.setProperty("spring.profiles.active", "test")
    }

    override fun extensions() = listOf(SpringExtension)
}