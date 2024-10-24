package com.flash.interview.messaging.sensitive_words_service.framework.config.app

import io.swagger.v3.oas.models.info.Info
import org.springdoc.core.models.GroupedOpenApi
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.config.Customizer
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.http.SessionCreationPolicy
import org.springframework.security.core.userdetails.User
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.security.provisioning.InMemoryUserDetailsManager
import org.springframework.security.web.SecurityFilterChain


@Configuration
class AppConfig(
    private var defaultUser: DefaultUserCredentialsConfig
) {

    @Bean
    fun filterChain(http: HttpSecurity): SecurityFilterChain {
        http
            .csrf{ it.disable() }
            .sessionManagement {
                it.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
            }
            .authorizeHttpRequests { auth ->
                auth
                    .requestMatchers("/api/v1/messages/admin/**").hasRole("admin")
                    .anyRequest().permitAll()
            }
            .httpBasic(Customizer.withDefaults())
        return http.build()
    }

    @Bean
    fun  userDetailsService(): InMemoryUserDetailsManager {
        println("This is the default user: $defaultUser")
        val user = User
            .withUsername(defaultUser.username)
            .password(passwordEncoder().encode(defaultUser.password))
            .roles(defaultUser.role)
            .build();
        return InMemoryUserDetailsManager(user)
    }

    @Bean
    fun passwordEncoder(): PasswordEncoder = BCryptPasswordEncoder(8)

    @Bean
    fun publicApi(): GroupedOpenApi {
        return GroupedOpenApi.builder()
            .group("public-api")
            .pathsToMatch("/api/**")
            .build()
    }

    @Bean
    fun apiInfo(): Info {
        return Info()
            .title("Sensitive Word Redaction API")
            .description("API that redacts sensitive words from messages.")
            .version("1.0.0")
    }

}