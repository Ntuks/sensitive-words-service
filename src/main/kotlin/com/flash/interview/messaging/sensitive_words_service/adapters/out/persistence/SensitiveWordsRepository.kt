package com.flash.interview.messaging.sensitive_words_service.adapters.out.persistence

import com.flash.interview.messaging.sensitive_words_service.adapters.out.entities.SensitiveWordEntity
import org.springframework.data.jpa.repository.JpaRepository

interface SensitiveWordsRepository: JpaRepository<SensitiveWordEntity, String>