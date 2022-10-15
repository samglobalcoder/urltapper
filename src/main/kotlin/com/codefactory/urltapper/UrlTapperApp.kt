package com.codefactory.urltapper

import io.swagger.v3.oas.annotations.OpenAPIDefinition
import io.swagger.v3.oas.annotations.info.Info
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

/**
 * This is bootstrap main class helps to kick-start the spring application.
 * @author sam
 * @Date 15/10/22
 * @createdBy intelij
 */
@OpenAPIDefinition(
    info = Info(
        title = "UrlTapper Rest Api",
        version = "0.0.1",
        description = "Tapping the length of the url and returning unique shortened url"
    )
)
@SpringBootApplication
class UrlTapperApp

fun main(args: Array<String>) {
    runApplication<UrlTapperApp>(*args)
}
