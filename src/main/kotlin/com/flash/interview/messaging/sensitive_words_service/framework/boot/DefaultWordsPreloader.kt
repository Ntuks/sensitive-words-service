package com.flash.interview.messaging.sensitive_words_service.framework.boot

import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.flash.interview.messaging.sensitive_words_service.adapters.out.entities.SensitiveWordEntity
import com.flash.interview.messaging.sensitive_words_service.adapters.out.persistence.SensitiveWordsRepository
import com.flash.interview.messaging.sensitive_words_service.domain.models.SensitiveWord
import org.springframework.boot.CommandLineRunner
import org.springframework.stereotype.Component
import java.io.File

@Component
class DefaultWordsPreloader (
        private val sensitiveWordsRepository: SensitiveWordsRepository
) : CommandLineRunner {
    override fun run(vararg args: String?) {
        // Check if the database is empty
        if (sensitiveWordsRepository.count() == 0L) {
            // Read the actions from the text file
            val file = File("src/main/resources/static/sql_sensitive_list.txt")
            val objectMapper = jacksonObjectMapper()

            // Parse the JSON array
            val actions: List<String> = objectMapper.readValue(file, List::class.java) as List<String>

            // Save actions to the database
            actions.map { SensitiveWordEntity(text = it) }.let { sensitiveWordsRepository.saveAll(it) }
        }
    }
}