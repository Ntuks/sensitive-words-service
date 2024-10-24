package com.flash.interview.messaging.sensitive_words_service.domain.application

import com.flash.interview.messaging.sensitive_words_service.domain.models.SensitiveWord
import com.flash.interview.messaging.sensitive_words_service.domain.ports.SensitiveWordsManagementPort
import org.junit.jupiter.api.Assertions.*

import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

class SensitiveWordServiceTest {

    private lateinit var managementPort: SensitiveWordsManagementPort
    private lateinit var sensitiveWordService: SensitiveWordService

    @BeforeEach
    fun setup() {
        managementPort = mockk()
        sensitiveWordService = SensitiveWordService(managementPort)
    }

    @Test
    fun `redactSensitiveWords should replace sensitive words with mask`() {
        val message = "This is a sensitive message with bad words."
        val sensitiveWords = listOf(
            SensitiveWord("1", "sensitive"),
            SensitiveWord("2", "bad")
        )
        every { managementPort.getSensitiveWords() } returns Result.success(sensitiveWords)

        val result = sensitiveWordService.redactSensitiveWords(message)

        assertTrue(result.isSuccess)
        val (redactedMessage, count) = result.getOrNull()!!
        assertEquals("This is a ********* message with ********* words.", redactedMessage.content)
        assertEquals(2, count)
    }

    @Test
    fun `redactSensitiveWords should handle case-insensitive matches`() {
        val message = "This is SENSITIVE and Bad."
        val sensitiveWords = listOf(
            SensitiveWord("1", "sensitive"),
            SensitiveWord("2", "bad")
        )
        every { managementPort.getSensitiveWords() } returns Result.success(sensitiveWords)

        val result = sensitiveWordService.redactSensitiveWords(message)

        assertTrue(result.isSuccess)
        val (redactedMessage, count) = result.getOrNull()!!
        assertEquals("This is ********* and *********.", redactedMessage.content)
        assertEquals(2, count)
    }

    @Test
    fun `redactSensitiveWords should return failure when getSensitiveWords fails`() {
        val message = "This is a test message."
        every { managementPort.getSensitiveWords() } returns Result.failure(Exception("Failed to get sensitive words."))

        val result = sensitiveWordService.redactSensitiveWords(message)

        assertTrue(result.isFailure)
        assertEquals("Failed to get sensitive words.", result.exceptionOrNull()?.message)
    }

    @Test
    fun `createSensitiveWords should delegate to managementPort`() {
        val sensitiveWords = setOf("word1", "word2")
        val expectedResult = Result.success(listOf(SensitiveWord("1", "word1"), SensitiveWord("2", "word2")))
        every { managementPort.createSensitiveWords(sensitiveWords) } returns expectedResult

        val result = sensitiveWordService.createSensitiveWords(sensitiveWords)

        assertEquals(expectedResult, result)
        verify { managementPort.createSensitiveWords(sensitiveWords) }
    }

    @Test
    fun `getSensitiveWords should delegate to managementPort`() {
        val expectedResult = Result.success(listOf(SensitiveWord("1", "word1"), SensitiveWord("2", "word2")))
        every { managementPort.getSensitiveWords() } returns expectedResult

        val result = sensitiveWordService.getSensitiveWords()

        assertEquals(expectedResult, result)
        verify { managementPort.getSensitiveWords() }
    }

    @Test
    fun `getSensitiveWord should delegate to managementPort`() {
        val id = "1"
        val expectedResult = Result.success(SensitiveWord(id, "word1"))
        every { managementPort.getSensitiveWord(id) } returns expectedResult

        val result = sensitiveWordService.getSensitiveWord(id)

        assertEquals(expectedResult, result)
        verify { managementPort.getSensitiveWord(id) }
    }

    @Test
    fun `updateSensitiveWords should delegate to managementPort`() {
        val sensitiveWord = SensitiveWord("1", "updatedWord")
        val expectedResult = Result.success(sensitiveWord)
        every { managementPort.updateSensitiveWords(sensitiveWord) } returns expectedResult

        val result = sensitiveWordService.updateSensitiveWords(sensitiveWord)

        assertEquals(expectedResult, result)
        verify { managementPort.updateSensitiveWords(sensitiveWord) }
    }

    @Test
    fun `deleteSensitiveWords should delegate to managementPort`() {
        val id = "1"
        val expectedResult = Result.success(Unit)
        every { managementPort.deleteSensitiveWord(id) } returns expectedResult

        val result = sensitiveWordService.deleteSensitiveWords(id)

        assertEquals(expectedResult, result)
        verify { managementPort.deleteSensitiveWord(id) }
    }
}
