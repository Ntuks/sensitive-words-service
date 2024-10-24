package com.flash.interview.messaging.sensitive_words_service.domain.application

import com.flash.interview.messaging.sensitive_words_service.domain.models.RedactedMessage
import com.flash.interview.messaging.sensitive_words_service.domain.models.SensitiveWord
import com.flash.interview.messaging.sensitive_words_service.domain.ports.SensitiveWordsManagementPort
import com.flash.interview.messaging.sensitive_words_service.domain.usecases.*
import org.springframework.stereotype.Service

@Service
class SensitiveWordService(
    private val managementPort: SensitiveWordsManagementPort
): CreateSensitiveWordsUseCase, GetSensitiveWordsUseCase,
    UpdateSensitiveWordsUseCase, DeleteSensitiveWordsUseCase, RedactSensitiveWordsUseCase
{

    companion object {
        const val WORD_MASK = "*********"
    }

    override fun redactSensitiveWords(message: String): Result<Pair<RedactedMessage, Int>> {
        var redactedMessage = message
        var redactedWordCount = 0
        val sensitiveWords = getSensitiveWords().getOrElse {
            return Result.failure(Error("Failed to get sensitive words."))
        }
        sensitiveWords.forEach { word ->
            val regex = Regex("\\b${Regex.escape(word.text)}\\b", RegexOption.IGNORE_CASE)
            redactedMessage = regex.replace(redactedMessage) { matchResult ->
                if (matchResult.groupValues.isNotEmpty()) redactedWordCount = redactedWordCount.plus(1)
                WORD_MASK
            }
        }
        return Result.success(RedactedMessage(content = redactedMessage) to redactedWordCount)
    }

    override fun createSensitiveWords(sensitiveWords: Set<String>): Result<List<SensitiveWord>> {
        return managementPort.createSensitiveWords(sensitiveWords)
    }

    override fun getSensitiveWords(): Result<List<SensitiveWord>> {
        return  managementPort.getSensitiveWords()
    }

    override fun getSensitiveWord(id: String): Result<SensitiveWord> {
        return  managementPort.getSensitiveWord(id)
    }


    override fun updateSensitiveWords(sensitiveWord: SensitiveWord): Result<SensitiveWord> {
        return managementPort.updateSensitiveWords(sensitiveWord)
    }

    override fun deleteSensitiveWords(id: String): Result<Unit> {
        return managementPort.deleteSensitiveWord(id)
    }

}