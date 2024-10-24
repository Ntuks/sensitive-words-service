package com.flash.interview.messaging.sensitive_words_service.adapters.`in`.controllers


import com.fasterxml.jackson.databind.ObjectMapper
import com.flash.interview.messaging.sensitive_words_service.adapters.`in`.dtos.CreateSensitiveWordsRequest
import com.flash.interview.messaging.sensitive_words_service.adapters.`in`.dtos.UpdateSensitiveWordRequest
import com.flash.interview.messaging.sensitive_words_service.domain.application.SensitiveWordService
import com.flash.interview.messaging.sensitive_words_service.domain.models.RedactedMessage
import com.flash.interview.messaging.sensitive_words_service.domain.models.SensitiveWord
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.mockito.Mockito.*
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.boot.test.mock.mockito.MockBean
import org.springframework.http.MediaType
import org.springframework.security.test.context.support.WithMockUser
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.*

@WebMvcTest(MessagingController::class)
class MessagingControllerTest {

    @Autowired
    private lateinit var mockMvc: MockMvc

    @MockBean
    private lateinit var sensitiveWordService: SensitiveWordService

    @Autowired
    private lateinit var objectMapper: ObjectMapper

    @BeforeEach
    fun setUp() {
        // Setup can be done here if needed
    }

    @Test
    @WithMockUser(username = "interviewUser", password = "reallyNotSafePassword", roles = ["admin"]) // Simulate a logged-in user for admin endpoints
    fun `should add sensitive word successfully`() {
        val request = CreateSensitiveWordsRequest(setOf("badword"))

        mockMvc.perform(
            post("/api/v1/messages/admin/addSensitiveWord")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request))
        )
            .andExpect(status().isOk)
            .andExpect(content().string("Word added successfully"))

        verify(sensitiveWordService, times(1)).createSensitiveWords(any())
    }

    @Test
    @WithMockUser
    fun `should get sensitive words successfully`() {
        val sensitiveWords = listOf("badword", "anotherBadWord")
        val expectedWords = sensitiveWords.mapIndexed { id: Int, test: String ->
            SensitiveWord(id.toString(), test)
        }
        `when`(sensitiveWordService.getSensitiveWords())
            .thenReturn(Result.success(expectedWords))

        mockMvc.perform(get("/api/v1/messages/admin/getSensitiveWords"))
            .andExpect(status().isOk)
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.sensitiveWords[0]").value("badword"))
            .andExpect(jsonPath("$.sensitiveWords[1]").value("anotherBadWord"))

        verify(sensitiveWordService, times(1)).getSensitiveWords()
    }

    @Test
    @WithMockUser
    fun `should update sensitive word successfully`() {
        val request = UpdateSensitiveWordRequest(id = "1", text = "updatedBadword")
        val updatedWord = SensitiveWord(id = "1", text = "updatedBadword")
        `when`(sensitiveWordService.updateSensitiveWords(any())).thenReturn(Result.success(updatedWord))

        mockMvc.perform(
            put("/api/v1/messages/admin/updateSensitiveWord")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request))
        )
            .andExpect(status().isOk)
            .andExpect(jsonPath("$.sensitiveWords[0].text").value("updatedBadword"))

        verify(sensitiveWordService, times(1)).updateSensitiveWords(any())
    }

    @Test
    @WithMockUser
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
    @WithMockUser
    fun `should redact message successfully`() {
        val message = "This is a badword in the message."
        val redactedMessage = "This is a ***** in the message."
        `when`(sensitiveWordService.redactSensitiveWords(message))
            .thenReturn(
                Result.success(RedactedMessage(redactedMessage) to 1)
            )

        mockMvc.perform(
            post("/api/v1/messages/redact")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(message))
        )
            .andExpect(status().isOk)
            .andExpect(content().string(redactedMessage))

        verify(sensitiveWordService, times(1)).redactSensitiveWords(message)
    }
}
