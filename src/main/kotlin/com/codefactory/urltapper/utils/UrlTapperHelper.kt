package com.codefactory.urltapper.utils

import com.google.common.hash.HashFunction
import com.google.common.hash.Hashing
import org.springframework.stereotype.Component
import java.nio.charset.StandardCharsets

/**
 * This class provides support methods which handles all the manipulation related
 * to string hashing etc.
 * @author sam
 * @Date 15/10/22
 * @createdBy intelij
 */
@Component
class UrlTapperHelper {

    /**
     * This method helps to perform the SHA-256 hashing and returns the hashed value.
     * @param strToBeHashed The string value to be hashed.
     */
    fun hashBySHA256WithUTF8(strToBeHashed: String): String {
        val hashFunObj: HashFunction = Hashing.sha256()
        val hashedVal = hashFunObj.hashString(strToBeHashed, StandardCharsets.UTF_8).toString()
        return hashedVal
    }
}