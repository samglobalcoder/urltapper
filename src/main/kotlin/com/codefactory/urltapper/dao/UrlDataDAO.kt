package com.codefactory.urltapper.dao

import com.codefactory.urltapper.dto.UrlTapRequest
import io.swagger.v3.oas.annotations.media.Schema
import java.time.LocalDateTime
import java.util.*
import javax.persistence.Column
import javax.persistence.Entity
import javax.persistence.Id
import javax.persistence.Table

/**
 * This entity class helps to store the tapped url information in the relational database.
 * @author sam
 * @Date 15/10/22
 * @createdBy intelij
 */
@Table(name = "urldata")
@Entity(name = "urldatadao")
@Schema(description = "DAO for storing the url data information")
class UrlDataDAO(
    @field:Schema(
        description = "auto generated id value",
        type = "string"
    )
    @Id
    var id: UUID = UUID.randomUUID(),

    @field:Schema(
        description = "long url link",
        type = "string"
    )
    @Column(name = "longurl", nullable = false, updatable = false)
    var longUrl: String? = null,

    @field:Schema(
        description = "short url link",
        type = "string"
    )
    @Column(name = "shorturl", nullable = false, updatable = false)
    var shortUrl: String,

    @field:Schema(
        description = "record created at date in the database",
        type = "date"
    )
    @Column(name = "created_at", nullable = false, updatable = false)
    var createdAt: LocalDateTime = LocalDateTime.now()
) {
    companion object {
        fun mapToEntity(tapRequest: UrlTapRequest, hashedUrl: String): UrlDataDAO =
            UrlDataDAO(
                longUrl = tapRequest.longUrl,
                shortUrl = hashedUrl
            )

    }
}





