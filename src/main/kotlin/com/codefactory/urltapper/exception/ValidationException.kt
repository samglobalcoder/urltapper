package com.codefactory.urltapper.exception

/**
 * This class helps to throw the validation exception
 * @author sam
 * @Date 15/10/22
 * @createdBy intelij
 */
class ValidationException (var description : String) : RuntimeException() {
    override val message: String?
        get() = super.message
}