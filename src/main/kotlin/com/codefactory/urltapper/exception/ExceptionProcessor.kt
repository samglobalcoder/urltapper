package com.codefactory.urltapper.exception

import org.apache.commons.lang3.exception.ExceptionUtils
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.RestControllerAdvice
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler

/**
 *
 * This is the rest controller advice which propagates error /exception scenario
 * at this api level.
 * @author sam
 * @Date 15/10/22
 * @createdBy intelij
 */
@RestControllerAdvice
class ExceptionProcessor : ResponseEntityExceptionHandler() {

    /**
     * This method helps to handle very critical unhandled scenario,
     */
    @ExceptionHandler(Exception::class)
    fun handleDefaultException(ex: Exception): ResponseEntity<ErrorMessage?>? {
        logger.error(" Unhandled Exception occurred , " + ExceptionUtils.getStackTrace(ex))
        val errMsg = ErrorMessage(
            ex.message
        )
        return ResponseEntity<ErrorMessage?>(errMsg, HttpStatus.INTERNAL_SERVER_ERROR)
    }

    /**
     * This method helps to populate the validation exception during field validation.
     */
    @ExceptionHandler(ValidationException::class)
    fun validationException(vx: ValidationException): ResponseEntity<ErrorMessage?>? {
        logger.error("Validation failure happened ,  " + ExceptionUtils.getStackTrace(vx))
        val errMsg = ErrorMessage(vx.description)
        return ResponseEntity<ErrorMessage?>(errMsg, HttpStatus.BAD_REQUEST)
    }


    /**
     * This method helps to populate the data not found exception during data missing.
     */
    @ExceptionHandler(DataNotFoundException::class)
    fun dataNotFoundException(dx: DataNotFoundException): ResponseEntity<ErrorMessage?>? {
        logger.error("Data Not Found failure happened ,  " + ExceptionUtils.getStackTrace(dx))
        val errMsg = ErrorMessage(dx.description)
        return ResponseEntity<ErrorMessage?>(errMsg, HttpStatus.NOT_FOUND)
    }
}