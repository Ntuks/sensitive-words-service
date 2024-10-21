package com.flash.interview.messaging.sensitive_words_service.adapters.out.persistence

import com.flash.interview.messaging.sensitive_words_service.domain.ports.SensitiveWordsManagementPort
import org.springframework.stereotype.Component

@Component
class PersistenceAdapter(
    private val:
): SensitiveWordsManagementPort {

    override fun getSensitiveWords(id: String): Result<List<String>> {
        TODO("Not yet implemented")
    }

    override fun createSensitiveWords(): Result<Unit> {
        TODO("Not yet implemented")
    }

    override fun updateSensitiveWords(): Result<Unit> {
        TODO("Not yet implemented")
    }

    override fun deleteSensitiveWord(): Result<String> {
        TODO("Not yet implemented")
    }
}