package md.gvart.buildtracker.domain

import md.gvart.buildtracker.domain.entity.TriggerType
import md.gvart.buildtracker.domain.entity.WorkflowEntity
import md.gvart.buildtracker.domain.entity.WorkflowRegistration
import md.gvart.buildtracker.domain.gateway.PersistWorkflow
import md.gvart.buildtracker.domain.gateway.ScheduleWorkflow
import md.gvart.buildtracker.domain.gateway.ValidateWorkflowRegistration

class RegisterWorkflowTrigger(
    private val validateWorkflowRegistration: ValidateWorkflowRegistration,
    private val persistWorkflow: PersistWorkflow,
    private val scheduleWorkflow: ScheduleWorkflow
) : (WorkflowRegistration) -> WorkflowEntity {
  override fun invoke(registration: WorkflowRegistration): WorkflowEntity {
    validateWorkflowRegistration(registration)

    val entity = persistWorkflow(registration)
    if (entity.triggerType == TriggerType.SCHEDULED) {
      scheduleWorkflow(entity)
    }

    return entity
  }
}
