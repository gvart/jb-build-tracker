package md.gvart.buildtracker.domain.exception

/** Base exception type for all custom exception */
abstract class DomainException(message: String) : RuntimeException(message)
