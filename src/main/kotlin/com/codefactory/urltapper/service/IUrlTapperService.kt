package com.codefactory.urltapper.service


import com.codefactory.urltapper.dto.UrlTapRequest
import com.codefactory.urltapper.dto.UrlTapResponse
import java.util.*

/**
 * This interface helps to provide all the service methods for url data sending / retrieving to the repository.
 * @author sam
 * @Date 15/10/22
 * @createdBy intelij
 */
interface IUrlTapperService {

    /**
     * This service method helps to either retrieve the existing hashed url or it will
     * generate the new hashed value and storing in the database.
     * @param urlTapRequest This is the request for reducing the url.
     */
    fun doTapUrl(urlTapRequest: UrlTapRequest): UrlTapResponse

    /**
     * This service method helps to retrieve the existing long url by hashed url.
     * @param shortUrl This is the short url passed to retrieve long url.     */
    fun retrieveLongUrl(shortUrl: String): Optional<String>
}