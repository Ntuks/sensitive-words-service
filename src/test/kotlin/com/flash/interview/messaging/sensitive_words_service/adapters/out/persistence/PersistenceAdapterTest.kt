package com.flash.interview.messaging.sensitive_words_service.adapters.out.persistence

import com.flash.interview.messaging.sensitive_words_service.adapters.out.entities.SensitiveWordEntity
import com.flash.interview.messaging.sensitive_words_service.domain.models.SensitiveWord
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.Assertions.*
import java.util.Optional

class PersistenceAdapterTest {

    private lateinit var sensitiveWordsRepository: SensitiveWordsRepository
    private lateinit var persistenceAdapter: PersistenceAdapter

    @BeforeEach
    fun setup() {
        sensitiveWordsRepository = mockk()
        persistenceAdapter = PersistenceAdapter(sensitiveWordsRepository)
    }

    @Test
    fun `createSensitiveWords should return success result when words are saved successfully`() {
        val inputWords = setOf("word1", "word2")
        val savedEntities = listOf(
            SensitiveWordEntity(0, "word1"),
            SensitiveWordEntity(1, "word2")
        )

        every { sensitiveWordsRepository.saveAll(any<Iterable<SensitiveWordEntity>>()) } returns savedEntities

        val result = persistenceAdapter.createSensitiveWords(inputWords)

        assertTrue(result.isSuccess)
        assertEquals(2, result.getOrNull()?.size)
        assertEquals("word1", result.getOrNull()?.get(0)?.text)
        assertEquals("word2", result.getOrNull()?.get(1)?.text)
    }

    @Test
    fun `createSensitiveWords should return failure result when an exception occurs`() {
        val inputWords = setOf("word1", "word2")

        every { sensitiveWordsRepository.saveAll(any<Iterable<SensitiveWordEntity>>()) } throws RuntimeException("Database error")

        val result = persistenceAdapter.createSensitiveWords(inputWords)

        assertTrue(result.isFailure)
        assertEquals("Error occurred while creating sensitive words list.", result.exceptionOrNull()?.message)
    }

    @Test
    fun `getSensitiveWords should return success result with all words`() {
        val savedEntities = listOf(
            SensitiveWordEntity(0, "word1"),
            SensitiveWordEntity(1, "word2")
        )

        every { sensitiveWordsRepository.findAll() } returns savedEntities

        val result = persistenceAdapter.getSensitiveWords()

        assertTrue(result.isSuccess)
        assertEquals(2, result.getOrNull()?.size)
        assertEquals("word1", result.getOrNull()?.get(0)?.text)
        assertEquals("word2", result.getOrNull()?.get(1)?.text)
    }

    @Test
    fun `getSensitiveWords should return failure result when an exception occurs`() {
        every { sensitiveWordsRepository.findAll() } throws RuntimeException("Database error")

        val result = persistenceAdapter.getSensitiveWords()

        assertTrue(result.isFailure)
        assertEquals("Error occurred while getting sensitive words.", result.exceptionOrNull()?.message)
    }

    @Test
    fun `getSensitiveWord should return success result with the requested word`() {
        val id = "0"
        val savedEntity = SensitiveWordEntity(0, "word1")

        every { sensitiveWordsRepository.findById(id) } returns Optional.of(savedEntity)

        val result = persistenceAdapter.getSensitiveWord(id)

        assertTrue(result.isSuccess)
        assertEquals("word1", result.getOrNull()?.text)
        assertEquals(id, result.getOrNull()?.id)
    }

    @Test
    fun `getSensitiveWord should return failure result when an exception occurs`() {
        val id = "1"
        every { sensitiveWordsRepository.findById(id) } throws RuntimeException("Database error")

        val result = persistenceAdapter.getSensitiveWord(id)

        assertTrue(result.isFailure)
        assertEquals("Error occurred while getting sensitive word with id: 1.", result.exceptionOrNull()?.message)
    }

    @Test
    fun `updateSensitiveWords should return success result when word is updated successfully`() {
        val sensitiveWord = SensitiveWord("1", "updatedWord")
        val existingEntity = SensitiveWordEntity(0, "oldWord")
        val updatedEntity = SensitiveWordEntity(1, "updatedWord")

        every { sensitiveWordsRepository.findById("1") } returns Optional.of(existingEntity)
        every { sensitiveWordsRepository.save(any()) } returns updatedEntity

        val result = persistenceAdapter.updateSensitiveWords(sensitiveWord)

        assertTrue(result.isSuccess)
        assertEquals("updatedWord", result.getOrNull()?.text)
        assertEquals("1", result.getOrNull()?.id)
    }

    @Test
    fun `updateSensitiveWords should return failure result when word is not found`() {
        val sensitiveWord = SensitiveWord("1", "updatedWord")

        every { sensitiveWordsRepository.findById("1") } returns Optional.empty()

        val result = persistenceAdapter.updateSensitiveWords(sensitiveWord)

        assertTrue(result.isFailure)
        assertEquals("Sensitive word with id: ${sensitiveWord.id} not found.", result.exceptionOrNull()?.message)
    }

    @Test
    fun `deleteSensitiveWord should return success result when word is deleted successfully`() {
        val id = "1"

        every { sensitiveWordsRepository.deleteById(id) } returns Unit

        val result = persistenceAdapter.deleteSensitiveWord(id)

        assertTrue(result.isSuccess)
        verify { sensitiveWordsRepository.deleteById(id) }
    }

    @Test
    fun `deleteSensitiveWord should return failure result when an exception occurs`() {
        val id = "1"

        every { sensitiveWordsRepository.deleteById(id) } throws RuntimeException("Database error")

        val result = persistenceAdapter.deleteSensitiveWord(id)

        assertTrue(result.isFailure)
        assertEquals("Error occurred while deleting sensitive words.", result.exceptionOrNull()?.message)
    }
}

