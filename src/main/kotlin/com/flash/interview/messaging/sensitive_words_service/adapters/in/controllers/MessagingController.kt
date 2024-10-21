package com.flash.interview.messaging.sensitive_words_service.adapters.`in`.controllers

import com.flash.interview.messaging.sensitive_words_service.adapters.`in`.dtos.MessageRequest
import com.flash.interview.messaging.sensitive_words_service.adapters.`in`.dtos.MessageResponse
import com.flash.interview.messaging.sensitive_words_service.adapters.`in`.dtos.MessageResponse.Companion.toMessageResponse
import com.flash.interview.messaging.sensitive_words_service.domain.application.SensitiveWordService
import jakarta.validation.Valid
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/v1/messages")
class MessagingController(private val sensitiveWordService: SensitiveWordService) {

    @PostMapping("/redact")
    fun redactMessage(@Valid @RequestBody request: MessageRequest): ResponseEntity<MessageResponse> {
        val originalMessage = request.message
        val redactedMessage = sensitiveWordService.redactSensitiveWords(originalMessage).getOrThrow()

        return ResponseEntity.ok(redactedMessage.toMessageResponse(originalMessage))
    }

    // Admin endpoint for updating sensitive words
    @PostMapping("/admin/addSensitiveWord")
    fun addSensitiveWord(@RequestParam word: String): ResponseEntity<String> {
        sensitiveWordService.addSensitiveWord(word)
        return ResponseEntity("Word added successfully", HttpStatus.OK)
    }

    @GetMapping("/admin/getSensitiveWords")
    fun getSensitiveWords(): ResponseEntity<Set<String>> {
        return ResponseEntity.ok(sensitiveWordService.getSensitiveWords())
    }
}
