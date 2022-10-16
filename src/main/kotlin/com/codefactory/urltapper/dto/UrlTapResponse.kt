package com.codefactory.urltapper.dto

import io.swagger.v3.oas.annotations.media.Schema

/**
 * This is the response class captures the short url information.
 * @author sam
 * @Date 15/10/22
 * @createdBy intelij
 */
class UrlTapResponse {
    @field:Schema(
        description = "hashed short link of the url",
        type = "string"
    )
    var shortUrl: String = ""
}