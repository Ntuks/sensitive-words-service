package com.flash.interview.messaging.sensitive_words_service.domain.usecases

import com.flash.interview.messaging.sensitive_words_service.domain.models.SensitiveWord

interface GetSensitiveWordsUseCase {
    fun getSensitiveWord(id: String): Result<SensitiveWord>
    fun getSensitiveWords(): Result<List<SensitiveWord>>
}