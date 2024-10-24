package com.flash.interview.messaging.sensitive_words_service.domain.ports

import com.flash.interview.messaging.sensitive_words_service.domain.models.SensitiveWord

interface SensitiveWordsManagementPort {
    fun createSensitiveWords(sensitiveWords: Set<String>): Result<List<SensitiveWord>>

    fun getSensitiveWord(id: String): Result<SensitiveWord>

    fun getSensitiveWords(): Result<List<SensitiveWord>>

    fun updateSensitiveWords(sensitiveWord: SensitiveWord): Result<SensitiveWord>

    fun deleteSensitiveWord(id: String): Result<Unit>
}