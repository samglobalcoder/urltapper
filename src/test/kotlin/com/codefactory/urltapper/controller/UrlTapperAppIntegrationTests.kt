package com.codefactory.urltapper.controller

import com.codefactory.urltapper.dto.UrlGetRequest
import com.codefactory.urltapper.dto.UrlGetResponse
import com.codefactory.urltapper.dto.UrlTapRequest
import com.codefactory.urltapper.dto.UrlTapResponse
import io.kotest.matchers.string.shouldContain
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertNotNull
import org.junit.jupiter.api.Order
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.web.client.TestRestTemplate
import org.springframework.http.HttpStatus
import org.springframework.jdbc.core.JdbcTemplate
import org.springframework.test.context.DynamicPropertyRegistry
import org.springframework.test.context.DynamicPropertySource
import org.testcontainers.containers.PostgreSQLContainer
import org.testcontainers.junit.jupiter.Container
import org.testcontainers.junit.jupiter.Testcontainers

/**
 * This is an minimalistic integration test.
 * @author sam
 * @Date 15/10/22
 * @createdBy intelij
 */
@Testcontainers
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class UrlTapperAppIntegrationTests(
    @Autowired val restTemplate: TestRestTemplate
) {

    var hashUrl = "9a45b9fefab03e8ab388b83fe3aaf76e48ea0329310a825e24c4f14f24a8d1f8"
    var longUrl = "https://us05web.zoom.us/postattendee?mn=tPFBecg7Z7s1m4DrQWrsGNXXAtddXCpZuNrY.dTlMh5V3UFCeUohv&id=22"


    @Autowired
    lateinit var jdbcTemplate: JdbcTemplate

    companion object {

        /**
         * Container building logic.
         */
        @Container
        val container = PostgreSQLContainer<Nothing>("postgres:14")
            .apply {
                withDatabaseName("url")
                withUsername("testcontainer")
                withPassword("testcontainer")
            }

        /**
         * Test container properties initialization
         */
        @JvmStatic
        @DynamicPropertySource
        fun properties(registry: DynamicPropertyRegistry) {
            registry.add("spring.datasource.url", container::getJdbcUrl)
            registry.add("spring.datasource.password", container::getPassword)
            registry.add("spring.datasource.username", container::getUsername)
        }
    }

    @Test
    fun `Assert the tapUrl() method  with success`() {
        val urlTapRequest = UrlTapRequest()
        urlTapRequest.longUrl = longUrl
        val resObj = restTemplate.postForEntity("/v1/tapurl", urlTapRequest, UrlTapResponse::class.java)
        assertNotNull(resObj)
        assertEquals(resObj.statusCode, HttpStatus.OK)
    }

    @Test
    fun `Assert the tapUrl() method  with failure`() {
        val urlTapRequest = UrlTapRequest()
        urlTapRequest.longUrl = ""
        val resObj = restTemplate.postForEntity("/v1/tapurl", urlTapRequest, UrlTapResponse::class.java)
        assertNotNull(resObj)
        assertEquals(resObj.statusCode, HttpStatus.BAD_REQUEST)
    }

    @Test
    fun `Assert the getUrl() method  with success`() {
        val urlGetRequest = UrlGetRequest()
        urlGetRequest.shortUrl = hashUrl
        val resObj = restTemplate.postForEntity("/v1/geturl", urlGetRequest, UrlGetResponse::class.java)
        assertNotNull(resObj)
        assertEquals(resObj.statusCode, HttpStatus.OK)
    }

    @Test
    fun `Assert the getUrl() method  with failure`() {
        val urlGetRequest = UrlGetRequest()
        urlGetRequest.shortUrl = ""
        val resObj = restTemplate.postForEntity("/v1/geturl", urlGetRequest, UrlGetResponse::class.java)
        assertNotNull(resObj)
        assertEquals(resObj.statusCode, HttpStatus.BAD_REQUEST)
    }

    @Test
    @Order(6)
    fun `when database is connected then it should be Postgres version 14_5`() {
        val actualDatabaseVersion = jdbcTemplate.queryForObject("SELECT version()", String::class.java)
        actualDatabaseVersion shouldContain "PostgreSQL 14.5"
    }

}
