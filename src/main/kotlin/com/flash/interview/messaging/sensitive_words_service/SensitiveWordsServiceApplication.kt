package com.flash.interview.messaging.sensitive_words_service

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class SensitiveWordsServiceApplication

fun main(args: Array<String>) {
	runApplication<SensitiveWordsServiceApplication>(*args)
}
