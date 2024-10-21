package com.flash.interview.messaging.sensitive_words_service.domain.usecases

fun interface UpdateSensitiveWordsUseCase {
    fun updateSensitiveWords(): Result<Unit>
}