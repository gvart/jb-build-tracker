package md.gvart.buildtracker.gateway.validation.strategy

import md.gvart.buildtracker.domain.entity.WorkflowRegistration
import md.gvart.buildtracker.gateway.validation.error.InvalidFieldValueException
import org.apache.logging.log4j.kotlin.Logging

/**
 * Does the basic validation steps which should be followed by each validator type,
 *
 * <side_note> All of these validation could be done via [javax.validation] and for complex
 * validation custom validators can be created and injected to the spring context, but in order to
 * keep it simple I did it manually. Also, the validations should be a bit more sophisticated (like
 * check if branches contains valid value, if project key exists, repo url is accessible, etc.
 * </side_node>
 */
abstract class AbstractWorkflowTypeValidator : WorkflowTypeValidator, Logging {

  override fun validate(registration: WorkflowRegistration) {
    logger.debug("Validating $registration")

    validateField(registration.repositoryUrl, "repositoryUrl")
    validateField(registration.projectKey, "projectKey")
    validateField(registration.pipelineFileName, "pipelineFileName")
    validateField(registration.branches, "branches")
  }

  private fun validateField(value: String, fieldName: String) {
    if (value.isBlank()) {
      throw InvalidFieldValueException("Field $fieldName shouldn't be blank")
    }
  }
  private fun validateField(value: List<String>, fieldName: String) {
    if (value.isEmpty()) {
      throw InvalidFieldValueException("Field $fieldName shouldn't be empty")
    }
  }
}
