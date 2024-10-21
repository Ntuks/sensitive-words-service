package com.flash.interview.messaging.sensitive_words_service.domain.usecases

import com.flash.interview.messaging.sensitive_words_service.domain.models.RedactedMessage

fun interface RedactSensitiveWordsUseCase {
    fun redactSensitiveWords(message: String): Result<RedactedMessage>
}