package com.flash.interview.messaging.sensitive_words_service.domain.usecases

import com.flash.interview.messaging.sensitive_words_service.domain.models.SensitiveWord

fun interface CreateSensitiveWordsUseCase {
    fun createSensitiveWords(sensitiveWords: Set<String>): Result<List<SensitiveWord>>
}