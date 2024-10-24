package com.flash.interview.messaging.sensitive_words_service.framework.config.app

import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.context.annotation.Configuration


@Configuration
@ConfigurationProperties("app.config.auth")
data class DefaultUserCredentialsConfig(
    @set:JvmName("setRole")
    var role: String? = null,
    @set:JvmName("setUsername")
    var username: String? = null,
    @set:JvmName("setPassword")
    var password: String? = null
)
