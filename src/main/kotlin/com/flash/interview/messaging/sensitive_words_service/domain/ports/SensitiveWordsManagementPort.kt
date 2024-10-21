package com.flash.interview.messaging.sensitive_words_service.domain.ports

interface SensitiveWordsManagementPort {
    fun getSensitiveWords(id: String): Result<List<String>>
    fun createSensitiveWords(): Result<Unit>
    fun updateSensitiveWords(): Result<Unit>
    fun deleteSensitiveWord(): Result<String>
}