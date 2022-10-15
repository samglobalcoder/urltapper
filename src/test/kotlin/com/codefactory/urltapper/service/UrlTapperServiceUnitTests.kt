package com.codefactory.urltapper.service

import com.codefactory.urltapper.dao.UrlDataDAO
import com.codefactory.urltapper.dto.UrlGetRequest
import com.codefactory.urltapper.dto.UrlTapRequest
import com.codefactory.urltapper.repo.IUrlTapperRepository
import com.codefactory.urltapper.service.impl.UrlTapperServiceImpl
import com.codefactory.urltapper.utils.UrlTapperHelper
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.Mockito
import org.mockito.MockitoAnnotations
import org.mockito.junit.jupiter.MockitoExtension
import org.springframework.test.context.event.annotation.AfterTestExecution
import java.util.*


/**
 * This mockito tests are an easy way to test the controller and other layers.
 * These tests enable the developer to debug the code instead of running heavy integration test.
 *
 * @author sam
 * @Date 15/10/22
 * @createdBy intelij
 */
@ExtendWith(MockitoExtension::class)
class UrlTapperServiceUnitTests {
    private val hashedUrl =
        "https://codefactory.com/01db1d92-d025-4df4-b010-3449a8eb1b8b"

    private val longUrl = "https://en.wikipedia.org/wiki/Example.com"

    private val noDomain =
        "https://www.test.com/9a45b9fefab03e8ab388b83fe3aaf76e48ea0329310a825e24c4f14f24a8d1f8"

    @BeforeEach
    fun setup() {
        MockitoAnnotations.openMocks(this)
    }

    @AfterTestExecution
    fun tearDown() {
        Mockito.clearAllCaches()
    }

    @Test
    fun `Assert doTapUrl() function success when passing correct request`() {
        val repo = Mockito.mock(IUrlTapperRepository::class.java)
        val helper = Mockito.spy(UrlTapperHelper::class.java)
        val tapperService = UrlTapperServiceImpl(repo, helper)
        Mockito.`when`(repo.findOneShortUrl(Mockito.anyString()))
            .thenReturn(Optional.of(buildUrlDataDAO()))
        val response = tapperService.doTapUrl(buildTapRequestSuccess())
        Assertions.assertNotNull(response)
    }

    @Test
    fun `Assert doTapUrl() function save scenario success`() {
        val repo = Mockito.mock(IUrlTapperRepository::class.java)
        val helper = Mockito.spy(UrlTapperHelper::class.java)
        val tapperService = UrlTapperServiceImpl(repo, helper)
        Mockito.`when`(repo.findOneShortUrl(Mockito.anyString()))
            .thenReturn(Optional.empty())
        Mockito.`when`(repo.save(Mockito.any(UrlDataDAO::class.java)))
            .thenReturn(buildUrlDataDAO())
        val response = tapperService.doTapUrl(buildTapRequestSuccess())
        Assertions.assertNotNull(response)
    }

    private fun buildTapRequestSuccess(): UrlTapRequest {
        val request = UrlTapRequest()
        request.longUrl = longUrl
        return request
    }

    private fun buildUrlDataDAO(): UrlDataDAO = UrlDataDAO(
        id = UUID.randomUUID(),
        shortUrl = hashedUrl,
        longUrl = longUrl
    )

    private fun buildUrlGetRequestShortUrl(): UrlGetRequest {
        val request = UrlGetRequest()
        request.shortUrl = hashedUrl
        return request
    }
}