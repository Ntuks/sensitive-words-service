package com.flash.interview.messaging.sensitive_words_service.domain.usecases

fun interface GetSensitiveWordsUseCase {
    fun getSensitiveWords(id: String): Result<List<String>>
}