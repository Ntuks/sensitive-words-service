package com.flash.interview.messaging.sensitive_words_service.adapters.`in`.controllers

import com.flash.interview.messaging.sensitive_words_service.adapters.`in`.dtos.*
import com.flash.interview.messaging.sensitive_words_service.adapters.`in`.dtos.MessageResponse.Companion.toMessageResponse
import com.flash.interview.messaging.sensitive_words_service.domain.application.SensitiveWordService
import com.flash.interview.messaging.sensitive_words_service.domain.models.SensitiveWord
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.media.Content
import io.swagger.v3.oas.annotations.media.Schema
import io.swagger.v3.oas.annotations.responses.ApiResponse
import io.swagger.v3.oas.annotations.responses.ApiResponses
import jakarta.validation.Valid
import org.springframework.data.repository.query.Param
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/v1/messages")
class MessagingController(private val sensitiveWordService: SensitiveWordService) {

    @Operation(summary = "Redact sensitive words from a message")
    @ApiResponses(
        value = [
            ApiResponse(
                responseCode = "200",
                description = "Message redacted successfully",
                content = [
                    Content(
                        mediaType = "application/json",
                        schema = Schema(implementation = MessageResponse::class)
                    )
                ]
            ),
            ApiResponse(responseCode = "400", description = "Invalid input"),
            ApiResponse(responseCode = "500", description = "Internal server error")
        ]
    )
    @PostMapping("/redact")
    fun redactMessage(@Valid @RequestBody request: MessageRequest): ResponseEntity<MessageResponse> {
        val originalMessage = request.message
        val (redactedMessage, redactedWordsCount) = sensitiveWordService.redactSensitiveWords(originalMessage).getOrThrow()

        return ResponseEntity.ok(redactedMessage.toMessageResponse(originalMessage, redactedWordsCount))
    }

    @Operation(
        summary = "Add new sensitive word(s)",
        description = "Adds one or more sensitive words to the database."
    )
    @ApiResponses(
        value = [
            ApiResponse(responseCode = "200", description = "Word added successfully"),
            ApiResponse(responseCode = "400", description = "Invalid input data")
        ]
    )
    @PostMapping("/admin/addSensitiveWord")
    fun addSensitiveWord(@Valid @RequestBody request: CreateSensitiveWordsRequest): ResponseEntity<String> {
        sensitiveWordService.createSensitiveWords(request.sensitiveWords)
        return ResponseEntity("Word added successfully", HttpStatus.OK)
    }

    @Operation(
        summary = "Get all sensitive words",
        description = "Retrieves a list of all sensitive words in the database."
    )
    @ApiResponses(
        value = [
            ApiResponse(responseCode = "200", description = "Sensitive words retrieved successfully"),
            ApiResponse(responseCode = "500", description = "Internal server error")
        ]
    )
    @GetMapping("/admin/getSensitiveWords")
    fun getSensitiveWords(): ResponseEntity<SensitiveWordsResponse> {
        val sensitiveWords = sensitiveWordService.getSensitiveWords().getOrThrow()

        val response = SensitiveWordsResponse(sensitiveWords)

        return ResponseEntity.ok(response)
    }

    @Operation(
        summary = "Update a sensitive word",
        description = "Updates an existing sensitive word with new text."
    )
    @ApiResponses(
        value = [
            ApiResponse(responseCode = "200", description = "Sensitive word updated successfully"),
            ApiResponse(responseCode = "404", description = "Sensitive word not found"),
            ApiResponse(responseCode = "400", description = "Invalid input data")
        ]
    )
    @PutMapping("/admin/updateSensitiveWord")
    fun updateSensitiveWord(
        @Valid @RequestBody request: UpdateSensitiveWordRequest
    ): ResponseEntity<SensitiveWordsResponse> {
        val sensitiveWord = SensitiveWord(id = request.id, text = request.text)

        val updated = sensitiveWordService.updateSensitiveWords(sensitiveWord).getOrThrow()
        val response = SensitiveWordsResponse(sensitiveWords = listOf(updated))

        return ResponseEntity.ok(response)
    }

    @Operation(
        summary = "Delete a sensitive word",
        description = "Deletes a sensitive word from the database using its ID."
    )
    @ApiResponses(
        value = [
            ApiResponse(responseCode = "204", description = "Sensitive word deleted successfully"),
            ApiResponse(responseCode = "404", description = "Sensitive word not found"),
            ApiResponse(responseCode = "400", description = "Invalid ID format")
        ]
    )
    @DeleteMapping("/admin/deleteSensitiveWord")
    fun deleteSensitiveWord(@Param(value = "id") id: String): ResponseEntity<Unit> {
        sensitiveWordService.deleteSensitiveWords(id)
        return ResponseEntity(HttpStatus.NO_CONTENT)
    }

}
