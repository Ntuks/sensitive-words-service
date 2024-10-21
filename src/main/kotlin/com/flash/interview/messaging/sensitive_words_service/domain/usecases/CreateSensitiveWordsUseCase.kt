package com.flash.interview.messaging.sensitive_words_service.domain.usecases

fun interface CreateSensitiveWordsUseCase {
    fun createSensitiveWords(): Result<Unit>
}