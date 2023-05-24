package md.gvart.buildtracker.entrypoint.web.exception

import md.gvart.buildtracker.domain.exception.DomainException
import md.gvart.buildtracker.domain.exception.ProjectWorkflowNotFound
import md.gvart.buildtracker.gateway.validation.error.InvalidFieldValueException
import md.gvart.buildtracker.gateway.validation.error.MissingRequiredOptionException
import org.apache.logging.log4j.kotlin.Logging
import org.springframework.http.HttpStatus
import org.springframework.http.ProblemDetail
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.RestControllerAdvice
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler

@RestControllerAdvice
class WebExceptionHandler : ResponseEntityExceptionHandler(), Logging {

  @ExceptionHandler(DomainException::class)
  fun handleDomainException(exception: DomainException): ProblemDetail {
    logger.warn("Request processing failed with the following exception", exception)
    return when (exception) {
      is ProjectWorkflowNotFound ->
          ProblemDetail.forStatusAndDetail(HttpStatus.NOT_FOUND, exception.message!!)
      is MissingRequiredOptionException ->
          ProblemDetail.forStatusAndDetail(HttpStatus.BAD_REQUEST, exception.message!!)
      is InvalidFieldValueException ->
          ProblemDetail.forStatusAndDetail(HttpStatus.BAD_REQUEST, exception.message!!)
      else -> {
        logger.error(
            "DomainException with no handing identifier, fallback to 500 response status code",
            exception)
        ProblemDetail.forStatus(HttpStatus.INTERNAL_SERVER_ERROR)
      }
    }
  }
}
