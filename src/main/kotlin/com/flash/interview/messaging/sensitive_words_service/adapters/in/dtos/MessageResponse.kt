package com.flash.interview.messaging.sensitive_words_service.adapters.`in`.dtos

import com.flash.interview.messaging.sensitive_words_service.domain.models.RedactedMessage

data class MessageResponse(
    val originalMessage: String,
    val redactedMessage: String,
    val redactedWordCount: Int
) {
    companion object {
        fun RedactedMessage.toMessageResponse(originalMessage: String) = MessageResponse (
            redactedMessage = message,
            originalMessage = originalMessage,
            redactedWordCount = sensitiveWords.size
        )
    }
}