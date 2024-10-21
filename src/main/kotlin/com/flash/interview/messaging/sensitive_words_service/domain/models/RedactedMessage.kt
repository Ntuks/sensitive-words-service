package com.flash.interview.messaging.sensitive_words_service.domain.models

data class RedactedMessage(
    val id: String,
    val message: String,
    val language: String,
    val sensitiveWords: List<String>,
    val originalMessageId: String,
)
