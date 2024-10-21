package com.flash.interview.messaging.sensitive_words_service.domain.application

import com.flash.interview.messaging.sensitive_words_service.domain.models.RedactedMessage
import com.flash.interview.messaging.sensitive_words_service.domain.ports.SensitiveWordsManagementPort
import com.flash.interview.messaging.sensitive_words_service.domain.usecases.*
import org.springframework.stereotype.Service

@Service
class SensitiveWordService(
    private val managementPort: SensitiveWordsManagementPort
): CreateSensitiveWordsUseCase, GetSensitiveWordsUseCase,
    UpdateSensitiveWordsUseCase, DeleteSensitiveWordsUseCase, RedactSensitiveWordsUseCase
{

    private val sensitiveWords: MutableSet<String> = mutableSetOf("password", "secret", "confidential")

    // Allows admins to update the sensitive word list dynamically
    fun addSensitiveWord(word: String) {
        sensitiveWords.add(word.toLowerCase())
    }

    fun getSensitiveWords(): Set<String> = sensitiveWords
    override fun getSensitiveWords(id: String): Result<List<String>> {
        return  managementPort.getSensitiveWords(id)
    }

    override fun createSensitiveWords(): Result<Unit> {
        return managementPort.createSensitiveWords()
    }

    override fun updateSensitiveWords(): Result<Unit> {
        return managementPort.updateSensitiveWords()
    }

    override fun deleteSensitiveWords(id: String): Result<String> {
        return managementPort.deleteSensitiveWord()
    }

    override fun redactSensitiveWords(message: String): Result<RedactedMessage> {
        var redactedMessage = message
        sensitiveWords.forEach { word ->
            val regex = Regex("\\b${Regex.escape(word)}\\b", RegexOption.IGNORE_CASE)
            redactedMessage = regex.replace(redactedMessage) { "[REDACTED]" }
        }
        return redactedMessage
    }
}