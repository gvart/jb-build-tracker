package md.gvart.buildtracker.gateway.validation

import md.gvart.buildtracker.domain.entity.TriggerType
import md.gvart.buildtracker.domain.entity.WorkflowRegistration
import md.gvart.buildtracker.domain.gateway.ValidateWorkflowRegistration
import md.gvart.buildtracker.gateway.validation.strategy.WorkflowTypeValidator
import org.apache.logging.log4j.kotlin.Logging
import org.springframework.stereotype.Component

@Component
class DefaultValidateWorkflowRegistration(validators: List<WorkflowTypeValidator>) :
    ValidateWorkflowRegistration, Logging {

  private val validationStrategy = mutableMapOf<TriggerType, WorkflowTypeValidator>()

  init {
    validators.forEach { validationStrategy[it.getType()] = it }
  }

  override fun invoke(registration: WorkflowRegistration) {
    validationStrategy[registration.triggerType]?.validate(registration)
        ?: logger.warn("Validator is missing for triggerType:${registration.triggerType}")
  }
}
