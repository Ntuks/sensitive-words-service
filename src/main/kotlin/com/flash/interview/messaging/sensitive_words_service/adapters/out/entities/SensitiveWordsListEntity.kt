package com.flash.interview.messaging.sensitive_words_service.adapters.out.entities

import com.flash.interview.messaging.sensitive_words_service.domain.models.SensitiveWord
import jakarta.persistence.Entity
import jakarta.persistence.Column
import jakarta.persistence.Id
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Table

@Entity
@Table(name = "sensitive_words")
data class SensitiveWordEntity(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long? = null,
    @Column(nullable = false, unique = true)
    val text: String,
) {
    constructor() : this(null, "")

    fun toSensitiveWordsList() = SensitiveWord(id.toString(), text)

    companion object {
        fun SensitiveWord.toEntity() = SensitiveWordEntity(id = id.toLongOrNull() ?: 0, text = text)
    }
}
