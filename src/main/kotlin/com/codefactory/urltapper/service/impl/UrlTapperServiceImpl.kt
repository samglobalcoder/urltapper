package com.codefactory.urltapper.service.impl

import com.codefactory.urltapper.constants.UrlTapperConstants
import com.codefactory.urltapper.dao.UrlDataDAO
import com.codefactory.urltapper.dto.UrlTapRequest
import com.codefactory.urltapper.dto.UrlTapResponse
import com.codefactory.urltapper.repo.IUrlTapperRepository
import com.codefactory.urltapper.service.IUrlTapperService
import com.codefactory.urltapper.utils.UrlTapperHelper
import org.springframework.stereotype.Service

/**
 * This is the service implementation for invoking repository layer and processing various business logic.
 * @author sam
 * @Date 15/10/22
 * @createdBy intelij
 */
@Service
class UrlTapperServiceImpl(
    val tapperRepository: IUrlTapperRepository,
    val tapperHelper: UrlTapperHelper
) : IUrlTapperService {

    /**
     * This service method helps to either retrieve the existing hashed url or it will
     * generate the new hashed value and storing in the database.
     * @param urlTapRequest This is the request for reducing the url.
     */
    override fun doTapUrl(urlTapRequest: UrlTapRequest): UrlTapResponse {
        val urlTapResponse = UrlTapResponse()
        //passing the long url and converting SHA256 string
        val hashedUrl = tapperHelper.hashBySHA256WithUTF8(urlTapRequest.longUrl)
        //verify the hashed value present in the database or not
        val shortUrl = tapperRepository.findOneShortUrl(hashedUrl)
        //if the hashed value is not available in the database, then new record has to be saved

        if (shortUrl.isEmpty) {
            //saving the newly hashed value into database
            val tapperDAO = UrlDataDAO.mapToEntity(urlTapRequest, hashedUrl)
            //store the record
            val urldata = tapperRepository.save(tapperDAO)
            urlTapResponse.shortUrl = UrlTapperConstants.URL_TAPPER_DOMAIN + urldata.id.toString().trim()
            return urlTapResponse
        }
        urlTapResponse.shortUrl = UrlTapperConstants.URL_TAPPER_DOMAIN + shortUrl.get().id.toString().trim()
        return urlTapResponse
    }

}