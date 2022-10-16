package com.codefactory.urltapper.controller

import com.codefactory.urltapper.dao.UrlDataDAO
import com.codefactory.urltapper.dto.UrlTapRequest
import com.codefactory.urltapper.dto.UrlTapResponse
import com.codefactory.urltapper.repo.IUrlTapperRepository
import org.junit.After
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertNotNull
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.web.client.TestRestTemplate
import org.springframework.http.HttpStatus
import org.springframework.test.context.DynamicPropertyRegistry
import org.springframework.test.context.DynamicPropertySource
import org.springframework.web.util.UriComponentsBuilder
import org.testcontainers.containers.PostgreSQLContainer
import org.testcontainers.junit.jupiter.Container
import org.testcontainers.junit.jupiter.Testcontainers
import java.time.LocalDateTime
import java.util.*

/**
 * This is a minimalistic integration test with test containers.
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

    var shortUrl = "https://cf.com/d8d74b37-60c7-4689-b08a-b40bd55874a6"
    var shortUrlNonAvailable = "https://cf.com/d8d74b37-60c7-4689-b08a-b40bd55874a9"
    var longUrl =
        "https://www.google.com/search?q=software+test+design+and+testing+methodologies&source=lnms&tbm=isch&sa=X&ved=2ahUKEwjak6vvreL6AhVkXnwKHVBHBowQ_AUoAXoECAEQAw&biw=1920&bih=944&dpr=1#imgrc=BGg06cJSEFrjiM"

    @Autowired
    lateinit var repository: IUrlTapperRepository

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
    fun `Assert the tapUrl() function , given valid request then validate the status code`() {
        //Given , This  is valid long url given for retrieving shortened url
        val urlTapRequest = UrlTapRequest()
        urlTapRequest.longUrl = longUrl
        //when we call the tapurl function
        val resObj = restTemplate.postForEntity("/v1/tapurl", urlTapRequest, UrlTapResponse::class.java)
        //then , we should receive the status code as 200 and response of shortened url
        assertNotNull(resObj)
        assertEquals(resObj.statusCode, HttpStatus.OK)
    }


    @Test
    fun `Assert the tapUrl() function ,given empty long url, then validate the status code with error message `() {
        //Given , This  is long url is empty
        val urlTapRequest = UrlTapRequest()
        urlTapRequest.longUrl = ""
        //when we call the tapurl function
        val resObj = restTemplate.postForEntity("/v1/tapurl", urlTapRequest, UrlTapResponse::class.java)
        //then , we should receive the status code as 400 and Validation exception message
        assertNotNull(resObj)
        assertEquals(resObj.statusCode, HttpStatus.BAD_REQUEST)
    }

    @Test
    fun `Assert the getUrl() function, given shorten url and then validate the status code `() {
        //Given , There is a shortened url in the db with hashed value
        val uid = UUID.randomUUID()
        val newShortUrl = "https://cf.com/" + uid
        this.repository.save(UrlDataDAO(uid, longUrl, newShortUrl, LocalDateTime.now()))
        //when , we call the get url function
        var uri = UriComponentsBuilder
            .fromUriString("/v1/geturl")
            .queryParam("shortUrl", newShortUrl).toUriString()
        val resObj = restTemplate.getForEntity(uri, String::class.java)
        //then we should receive the status code of 302 and expected url redirection
        assertNotNull(resObj)
        assertEquals(resObj.statusCode, HttpStatus.FOUND)
    }

    @Test
    fun `Assert the getUrl() function, given shorten url and then validate the status code and message`() {
        //Given , there is a Shorter url which is not exist and when we call the get url function
        val uri = UriComponentsBuilder
            .fromUriString("/v1/geturl")
            .queryParam("shortUrl", shortUrlNonAvailable).toUriString()
        val resObj = restTemplate.getForEntity(uri, String::class.java)
        //then we should receive the status code of 404 and expected erroe message
        assertNotNull(resObj)
        assertEquals(resObj.statusCode, HttpStatus.NOT_FOUND)
    }

    @Test
    fun `Assert the getUrl() function ,given empty shorten url and then validate the status code`() {
        //Given , there is a Shorter url is empty
        //when we call the  geturl function
        val uri = UriComponentsBuilder
            .fromUriString("/v1/geturl")
            .queryParam("shortUrl", "").toUriString()
        val resObj = restTemplate.getForEntity(uri, String::class.java)
        //then we should receive the status code of 40o and error message
        assertNotNull(resObj)
        assertEquals(resObj.statusCode, HttpStatus.BAD_REQUEST)
    }

    @After
    fun `clear up all records`() {
        repository.deleteAll()
    }


}
