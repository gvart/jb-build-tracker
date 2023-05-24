package md.gvart.buildtracker.gateway.validation.strategy

import md.gvart.buildtracker.domain.entity.TriggerType
import md.gvart.buildtracker.domain.entity.WorkflowRegistration

/**
 * Main abstraction for building validations of [WorkflowRegistration] for different [TriggerType]
 */
interface WorkflowTypeValidator {

  fun getType(): TriggerType

  fun validate(registration: WorkflowRegistration)
}
