package md.gvart.buildtracker.gateway.validation.error

import md.gvart.buildtracker.domain.exception.DomainException

class InvalidFieldValueException(message: String) : DomainException(message)
