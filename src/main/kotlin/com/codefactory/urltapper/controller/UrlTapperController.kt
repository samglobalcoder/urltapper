package com.codefactory.urltapper.controller

import com.codefactory.urltapper.dto.UrlGetRequest
import com.codefactory.urltapper.dto.UrlGetResponse
import com.codefactory.urltapper.dto.UrlTapRequest
import com.codefactory.urltapper.dto.UrlTapResponse
import com.codefactory.urltapper.exception.ValidationException
import com.codefactory.urltapper.service.IUrlTapperService
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

/**
 * This is a rest controller which expose different functionality for the
 * URL tapping capability.
 *
 * <p> This application supports shortening of long urls , retrieving the existing
 * lengthy url stored.
 *  What is url shortening ?  @see https://en.wikipedia.org/wiki/URL_shortening
 * </p>
 *
 * @author sam
 * @Date 15/10/22
 * @createdBy intelij
 */
@RestController
@Tag(name = "UrlTapper Rest Api", description = "Tapping the length of the url and returning unique shortened url")
@RequestMapping("/v1")
class UrlTapperController(@Autowired val tapperService: IUrlTapperService) {

    /**
     * This API method helps to reduce or tap the given link or url.
     */
    @PostMapping(path = ["/tapurl"], produces = [MediaType.APPLICATION_JSON_VALUE])
    @Operation(
        description = "shorten the given long url",
        requestBody = io.swagger.v3.oas.annotations.parameters.RequestBody()
    )
    @ResponseBody
    fun tapUrl(@RequestBody urlTapRequest: UrlTapRequest)
            : ResponseEntity<UrlTapResponse> {
        val response: UrlTapResponse
        if (urlTapRequest.longUrl.isEmpty()) {
            throw ValidationException(" Long url not found in the request")
        }
        response = tapperService.doTapUrl(urlTapRequest)
        return ResponseEntity<UrlTapResponse>(response, HttpStatus.OK)
    }

    /**
     * This API method helps to retrieve the long url based on hashed short url.
     */
    @PostMapping(path = ["/geturl"], produces = [MediaType.APPLICATION_JSON_VALUE])
    @Operation(
        description = "retrieve the long url",
        requestBody = io.swagger.v3.oas.annotations.parameters.RequestBody()
    )
    @ResponseBody
    fun getUrl(@RequestBody urlGetRequest: UrlGetRequest)
            : ResponseEntity<UrlGetResponse> {
        val urlGetResponse = UrlGetResponse()
        if (urlGetRequest.shortUrl.isEmpty()) {
            //TODO: Validation Exception
            return ResponseEntity<UrlGetResponse>(urlGetResponse, HttpStatus.BAD_REQUEST)
        }
        //TODO: Call the service
        return ResponseEntity<UrlGetResponse>(urlGetResponse, HttpStatus.OK)
    }

}