package com.flash.interview.messaging.sensitive_words_service.adapters.`in`.dtos

import com.flash.interview.messaging.sensitive_words_service.domain.models.SensitiveWord

data class SensitiveWordsResponse(
    val sensitiveWords: List<SensitiveWord>
)
