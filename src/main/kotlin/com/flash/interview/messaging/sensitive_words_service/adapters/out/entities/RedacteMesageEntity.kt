package com.flash.interview.messaging.sensitive_words_service.adapters.out.entities

import com.flash.interview.messaging.sensitive_words_service.domain.models.RedactedMessage

data class RedactedMessageEntity(
    val id: String,
    val message: String,
    val language: String,
    val sensitiveWords: List<String>,
    val originalMessageId: String,
) {
    fun toRedactedMessage() = RedactedMessage(
        id,
        message,
        language,
        sensitiveWords,
        originalMessageId
    )

    companion object {
        fun RedactedMessage.toEntity() = RedactedMessageEntity(
            id,
            message,
            language,
            sensitiveWords,
            originalMessageId
        )
    }
}
