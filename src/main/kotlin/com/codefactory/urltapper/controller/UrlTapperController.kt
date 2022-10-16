package com.codefactory.urltapper.controller

import com.codefactory.urltapper.constants.UrlTapperConstants
import com.codefactory.urltapper.dto.UrlTapRequest
import com.codefactory.urltapper.dto.UrlTapResponse
import com.codefactory.urltapper.exception.DataNotFoundException
import com.codefactory.urltapper.exception.ValidationException
import com.codefactory.urltapper.service.IUrlTapperService
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.responses.ApiResponse
import io.swagger.v3.oas.annotations.responses.ApiResponses
import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import java.net.URI

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
    @ApiResponses(
        ApiResponse(responseCode = "200", description = "if the long url successfully generated the short url"),
        ApiResponse(responseCode = "400", description = "if the long url not found on the request")
    )
    @ResponseBody
    fun tapUrl(@RequestBody urlTapRequest: UrlTapRequest)
            : ResponseEntity<UrlTapResponse> {
        if (urlTapRequest.longUrl.isEmpty()) {
            throw ValidationException(" Long url not found in the request")
        }
        val response: UrlTapResponse = tapperService.doTapUrl(urlTapRequest)
        return ResponseEntity<UrlTapResponse>(response, HttpStatus.OK)
    }

    /**
     * This API method helps to retrieve the long url based on hashed short url.
     */
    @GetMapping(path = ["/geturl"])
    @Operation(
        description = "retrieve the long url",
        requestBody = io.swagger.v3.oas.annotations.parameters.RequestBody()
    )
    @ApiResponses(
        ApiResponse(
            responseCode = "302",
            description = "if the short url fetched and redirection happened successfully"
        ),
        ApiResponse(responseCode = "400", description = "if the shore url not found on the request"),
        ApiResponse(responseCode = "404", description = "if the short url not found on the database")
    )
    @ResponseBody
    fun getUrl(@RequestParam shortUrl: String)
            : ResponseEntity<String> {
        if (shortUrl.isEmpty() ||
            !shortUrl.startsWith(UrlTapperConstants.URL_TAPPER_DOMAIN)
        ) {
            throw ValidationException("Given short url is invalid")
        }
        val longUrl = tapperService
            .retrieveLongUrl(shortUrl.trim())
        if (longUrl.isPresent) {
            //url redirection happen in postman and not at swagger
            return ResponseEntity.status(HttpStatus.FOUND)
                .location(URI.create(longUrl.get()))
                .body("success")
        } else {
            //throw validation exception
            throw DataNotFoundException(" Long link not found , please create new short link")
        }
    }

}