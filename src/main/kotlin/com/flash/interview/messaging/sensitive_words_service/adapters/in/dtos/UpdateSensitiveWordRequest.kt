package com.flash.interview.messaging.sensitive_words_service.adapters.`in`.dtos

import jakarta.validation.constraints.NotBlank

data class UpdateSensitiveWordRequest(
    @field:NotBlank(message = "Sensitive words id must not be blank")
    val id: String,
    @field:NotBlank(message = "Sensitive words text must not be blank")
    val text: String
)
