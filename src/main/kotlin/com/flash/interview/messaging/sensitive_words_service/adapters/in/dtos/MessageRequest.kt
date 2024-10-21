package com.flash.interview.messaging.sensitive_words_service.adapters.`in`.dtos

import jakarta.validation.constraints.NotBlank

data class MessageRequest(
    @field:NotBlank(message = "Message must not be blank")
    val message: String
)
