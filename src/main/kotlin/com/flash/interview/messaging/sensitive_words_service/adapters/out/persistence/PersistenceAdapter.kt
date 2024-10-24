package com.flash.interview.messaging.sensitive_words_service.adapters.out.persistence

import com.flash.interview.messaging.sensitive_words_service.adapters.out.entities.SensitiveWordEntity
import com.flash.interview.messaging.sensitive_words_service.domain.models.SensitiveWord
import com.flash.interview.messaging.sensitive_words_service.domain.ports.SensitiveWordsManagementPort
import io.github.oshai.kotlinlogging.KotlinLogging
import org.springframework.stereotype.Component
import kotlin.jvm.optionals.getOrNull

@Component
class PersistenceAdapter(
    private val sensitiveWordsRepository: SensitiveWordsRepository
): SensitiveWordsManagementPort {

    private val logger = KotlinLogging.logger {}

    override fun createSensitiveWords(sensitiveWords: Set<String>): Result<List<SensitiveWord>> {
        logger.info { "Adding sensitive words." }
        try {
            val sensitiveWordsList = sensitiveWords.map { SensitiveWordEntity(text = it) }

            val savedSensitiveWords = sensitiveWordsRepository.saveAll(sensitiveWordsList)
            logger.info { "Successfully added sensitive words." }

            return Result.success(savedSensitiveWords.map { it.toSensitiveWordsList() })
        } catch (ex: Exception) {
            logger.error(ex) { "Failed to add sensitive words." }
            return Result.failure(Error("Error occurred while creating sensitive words list."))
        }
    }

    override fun getSensitiveWords(): Result<List<SensitiveWord>> {
        logger.info { "Getting sensitive words." }
        try {
            val sensitiveWords = sensitiveWordsRepository.findAll().map { it.toSensitiveWordsList() }
            logger.info { "Successfully got all the sensitive words." }
            return Result.success(sensitiveWords)
        } catch (ex: Exception) {
            return Result.failure(Error("Error occurred while getting sensitive words."))
        }
    }

    override fun getSensitiveWord(id: String): Result<SensitiveWord> {
        logger.info { "Getting a sensitive word with id: ${id}." }
        try {
            val sensitiveWords = sensitiveWordsRepository.findById(id).map { it.toSensitiveWordsList() }.get()
            logger.info { "Successfully added sensitive word with id: ${id}." }
            return Result.success(sensitiveWords)
        } catch (ex: Exception) {
            return Result.failure(Error("Error occurred while getting sensitive word with id: ${id}."))
        }
    }

    override fun updateSensitiveWords(sensitiveWord: SensitiveWord): Result<SensitiveWord> {
        logger.info { "Updating a sensitive word with id: ${sensitiveWord.id}." }
        try {
            val existingSensitiveWord = sensitiveWordsRepository.findById(sensitiveWord.id).getOrNull()

            val updatedSensitiveWord = existingSensitiveWord?.copy(text = sensitiveWord.text)
                ?: return Result.failure(Error("Sensitive word with id: ${sensitiveWord.id} not found."))

            val saveSensitiveWords = sensitiveWordsRepository.save(updatedSensitiveWord).toSensitiveWordsList()

            logger.info { "Successfully updated the sensitive word with id: ${sensitiveWord.id}." }
            return Result.success(saveSensitiveWords)
        } catch (ex: Exception) {
            logger.info { "Failed to update the sensitive word with id: ${sensitiveWord.id}." }
            return Result.failure(Error("Error occurred while updating sensitive words list."))
        }
    }

    override fun deleteSensitiveWord(id: String): Result<Unit> {
        logger.info { "Deleting the sensitive word with id: $id." }
        try {
            sensitiveWordsRepository.deleteById(id)

            logger.info { "Successfully deleted the sensitive word with id: $id." }
            return Result.success(Unit)
        } catch (ex: Exception) {
            logger.info { "Failed to delete the sensitive word with id: $id." }
            return Result.failure(Error("Error occurred while deleting sensitive words."))
        }
    }
}