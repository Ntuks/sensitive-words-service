package com.flash.interview.messaging.sensitive_words_service.framework.config

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.web.SecurityFilterChain

@Configuration
class AuthConfig {

    @Bean
    fun filterChain(http: HttpSecurity): SecurityFilterChain {
        http.csrf().disable()
            .authorizeHttpRequests {
                it.antMatchers("/api/v1/messages/admin/**").authenticated() // Admin endpoints require authentication
                it.anyRequest().permitAll()
            }
            .httpBasic() // Use basic auth for simplicity
        return http.build()
    }
}