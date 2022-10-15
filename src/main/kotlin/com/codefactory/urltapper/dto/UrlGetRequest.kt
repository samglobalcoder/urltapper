package com.codefactory.urltapper.dto

import io.swagger.v3.oas.annotations.media.Schema

/**
 * This request class responsible for the retrieve the long url by sending hashed url value.
 * @author sam
 * @Date 15/10/22
 * @createdBy intelij
 */
class UrlGetRequest() {
    @field:Schema(
        description = "short link of the url", type = "string"
    )
    var shortUrl: String = ""
        get() = field
        set(value) {
            field = value
        }
}