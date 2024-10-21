package com.flash.interview.messaging.sensitive_words_service.domain.models

import org.apache.catalina.valves.rewrite.InternalRewriteMap.LowerCase

data class SensitiveWordsList(
    val language: String,
    val lowerCaseWords: List<String>,
    val upperCaseWords: List<String>,
)
