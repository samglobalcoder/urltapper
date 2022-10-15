package com.codefactory.urltapper.dto

import io.swagger.v3.oas.annotations.media.Schema

/**
 * This  request class holds the long url for shortening.
 * @author sam
 * @Date 15/10/22
 * @createdBy intelij
 */
class UrlTapRequest() {
    @field:Schema(
        description = "long link of the url", type = "string"
    )
    var longUrl: String = ""
        get() = field
        set(value) {
            field = value
        }
}