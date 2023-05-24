package md.gvart.buildtracker.gateway.validation.strategy

import md.gvart.buildtracker.domain.entity.TriggerType
import md.gvart.buildtracker.domain.entity.WorkflowRegistration
import md.gvart.buildtracker.gateway.validation.error.MissingRequiredOptionException
import org.springframework.stereotype.Component

@Component
class ScheduledWorkflowTypeValidator : AbstractWorkflowTypeValidator() {

  companion object {
    private const val REQUIRED_PARAMETER_NAME = "cronExpression"
  }

  override fun getType() = TriggerType.SCHEDULED

  /**
   * <side_note>: also here we may do an extra validations, i.e.
   * 1. don't allow users to create cron expressions which will trigger build too often, so our
   *    system gets overloaded
   * 2. Check how many builds per hour/day/week a customer can run and if cron expression is with in
   *    </side_note>
   */
  override fun validate(registration: WorkflowRegistration) {
    super.validate(registration)
    if (!registration.options.containsKey(REQUIRED_PARAMETER_NAME)) {
      throw MissingRequiredOptionException(getType(), REQUIRED_PARAMETER_NAME)
    }
  }
}
