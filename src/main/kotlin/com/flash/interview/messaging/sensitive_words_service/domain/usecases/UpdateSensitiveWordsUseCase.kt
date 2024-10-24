package com.flash.interview.messaging.sensitive_words_service.domain.usecases

import com.flash.interview.messaging.sensitive_words_service.domain.models.SensitiveWord

fun interface UpdateSensitiveWordsUseCase {
    fun updateSensitiveWords(sensitiveWord: SensitiveWord): Result<SensitiveWord>
}