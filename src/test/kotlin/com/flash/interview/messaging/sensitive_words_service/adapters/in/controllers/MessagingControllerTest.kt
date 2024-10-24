package com.flash.interview.messaging.sensitive_words_service.adapters.`in`.controllers


import com.fasterxml.jackson.databind.ObjectMapper
import com.flash.interview.messaging.sensitive_words_service.adapters.`in`.dtos.*
import com.flash.interview.messaging.sensitive_words_service.domain.application.SensitiveWordService
import com.flash.interview.messaging.sensitive_words_service.domain.application.TestSecurityConfig
import com.flash.interview.messaging.sensitive_words_service.domain.models.RedactedMessage
import com.flash.interview.messaging.sensitive_words_service.domain.models.SensitiveWord
import io.mockk.every
import org.junit.jupiter.api.Test
import org.mockito.Mockito.*
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.boot.test.mock.mockito.MockBean
import org.springframework.context.annotation.Import
import org.springframework.http.MediaType
import org.springframework.security.test.context.support.WithMockUser
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.*

@WebMvcTest(MessagingController::class)
@Import(TestSecurityConfig::class)
class MessagingControllerTest {

    @Autowired
    private lateinit var mockMvc: MockMvc

    @MockBean
    private lateinit var sensitiveWordService: SensitiveWordService

    @Autowired
    private lateinit var objectMapper: ObjectMapper


    @Test
    @WithMockUser(username = "admin", password = "password", roles = ["admin"]) // Simulate a logged-in user for admin endpoints
    fun `should add sensitive word successfully`() {
        val request = CreateSensitiveWordsRequest(setOf("badword"))

        mockMvc.perform(
            post("/api/v1/messages/admin/addSensitiveWord")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
            .andDo { println(it.response.contentAsString) }
            .andDo { println(it.response.status) }
            .andDo { println(it.response.errorMessage) }
            .andExpect(status().isOk)
            .andExpect(content().string("Word added successfully"))

        verify(sensitiveWordService, times(1)).createSensitiveWords(request.sensitiveWords)
    }

    @Test
    @WithMockUser(username = "admin", password = "admin", roles = ["admin"])
    fun `should get sensitive words successfully`() {
        val sensitiveWords = listOf("badword", "anotherBadWord")
        val sensitiveWordsResponse = SensitiveWordsResponse(
            sensitiveWords = listOf(
                SensitiveWord(id = "0", text = sensitiveWords[0]),
                SensitiveWord(id = "1", text = sensitiveWords[1])
            )
        )
        val expectedWords = sensitiveWords.mapIndexed { id: Int, test: String ->
            SensitiveWord(id.toString(), test)
        }
        `when`(sensitiveWordService.getSensitiveWords())
            .thenReturn(Result.success(expectedWords))

        mockMvc.perform(get("/api/v1/messages/admin/getSensitiveWords"))
            .andDo { println(it.response.contentAsString) }
            .andDo { println(it.response.status) }
            .andDo { println(it.response.errorMessage) }
            .andExpect(status().isOk)
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(content().string(objectMapper.writeValueAsString(sensitiveWordsResponse)))

        verify(sensitiveWordService, times(1)).getSensitiveWords()
    }

    @Test
    @WithMockUser(username = "admin", password = "admin", roles = ["admin"])
    fun `should update sensitive word successfully`() {
        val request = UpdateSensitiveWordRequest(id = "1", text = "firstWord")
        val updatedWord = SensitiveWord(id = "1", text = "updatedBadword")
        val response = SensitiveWordsResponse(listOf(updatedWord))
        `when`(sensitiveWordService.updateSensitiveWords(
            sensitiveWord = SensitiveWord(request.id, request.text)
        )).thenReturn(Result.success(updatedWord))

        mockMvc.perform(
            put("/api/v1/messages/admin/updateSensitiveWord")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request))
        )
            .andDo { println(it.response.contentAsString) }
            .andDo { println(it.response.status) }
            .andDo { println(it.response.errorMessage) }
            .andExpect(status().isOk)
            .andExpect(content().string(objectMapper.writeValueAsString(response)))

        verify(sensitiveWordService, times(1)).updateSensitiveWords(updatedWord.copy(text = request.text))
    }

    @Test
    @WithMockUser(username = "admin", password = "password", roles = ["admin"])
    fun `should delete sensitive word successfully`() {
        val id = "1"

        mockMvc.perform(
            delete("/api/v1/messages/admin/deleteSensitiveWord")
                .param("id", id)
        )
            .andExpect(status().isNoContent)

        verify(sensitiveWordService, times(1)).deleteSensitiveWords(id)
    }

    @Test
    @WithMockUser(username = "admin", password = "password", roles = ["admin"])
    fun `should redact message successfully`() {
        val request = MessageRequest("This is a badword in the message.")
        val redactedMessage = RedactedMessage("This is a ***** in the message.")
        val response = MessageResponse(
            originalMessage = request.message,
            redactedMessage = redactedMessage.content,
            redactedWordsCount = 1
        )
        `when`(sensitiveWordService.redactSensitiveWords(request.message))
            .thenReturn(
                Result.success(redactedMessage to 1)
            )

        mockMvc.perform(
            post("/api/v1/messages/redact")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request))
        )
            .andDo { println(it.response.contentAsString) }
            .andDo { println(it.response.status) }
            .andDo { println(it.response.errorMessage) }
            .andExpect(status().isOk)
            .andExpect(content().string(objectMapper.writeValueAsString(response)))

        verify(sensitiveWordService, times(1)).redactSensitiveWords(request.message)
    }
}
