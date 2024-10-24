package com.flash.interview.messaging.sensitive_words_service.adapters.`in`.dtos

import jakarta.validation.constraints.NotEmpty

data class CreateSensitiveWordsRequest(
    @field:NotEmpty(message = "Sensitive words set must not be empty")
    val sensitiveWords: Set<String>
)
