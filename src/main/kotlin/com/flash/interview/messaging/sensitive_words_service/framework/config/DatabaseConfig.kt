package com.flash.interview.messaging.sensitive_words_service.framework.config

import org.springframework.boot.jdbc.DataSourceBuilder
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import javax.sql.DataSource

//@Configuration
//class DatabaseConfig(private val databaseProperties: DatabaseCredentialsConfig) {
//
//    @Bean
//    fun dataSource(): DataSource {
//        println(" The database credentials: $databaseProperties")
//        return DataSourceBuilder.create()
//            .url(databaseProperties.url)
//            .username(databaseProperties.username)
//            .password(databaseProperties.password)
//            .driverClassName("com.microsoft.sqlserver.jdbc.SQLServerDriver")
//            .build()
//    }
//}
