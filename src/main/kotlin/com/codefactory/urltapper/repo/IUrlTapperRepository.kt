package com.codefactory.urltapper.repo

import com.codefactory.urltapper.dao.UrlDataDAO
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import java.util.*

/**
 * This interface helps to provide all crud operation related to Url data storing / retrieving  / deleting.
 * @author sam
 * @Date 15/10/22
 * @createdBy intelij
 */
interface IUrlTapperRepository : JpaRepository<UrlDataDAO, String> {
    /**
     * This method helps to query the database and retrieve shorten hashed link.
     * @param hashedUrl This refers to the hashed url string.
     */
    @Query(value = "SELECT u FROM urldatadao u WHERE u.shortUrl = ?1")
    fun findOneShortUrl(hashedUrl: String): Optional<UrlDataDAO>

}