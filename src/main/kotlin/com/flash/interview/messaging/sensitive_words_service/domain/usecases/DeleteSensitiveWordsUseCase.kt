package com.flash.interview.messaging.sensitive_words_service.domain.usecases

fun interface DeleteSensitiveWordsUseCase {
    fun deleteSensitiveWords(id: String): Result<Unit>
}