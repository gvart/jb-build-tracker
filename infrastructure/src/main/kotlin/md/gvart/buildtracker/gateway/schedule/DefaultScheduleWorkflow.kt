package md.gvart.buildtracker.gateway.schedule

import md.gvart.buildtracker.domain.NotifyWorkflowTriggered
import md.gvart.buildtracker.domain.entity.WorkflowEntity
import md.gvart.buildtracker.domain.gateway.ScheduleWorkflow
import org.apache.logging.log4j.kotlin.Logging
import org.jobrunr.scheduling.JobScheduler
import org.springframework.stereotype.Component

@Component
class DefaultScheduleWorkflow(
    private val jobScheduler: JobScheduler,
    private val notifyWorkflowTriggered: NotifyWorkflowTriggered,
) : ScheduleWorkflow, Logging {
  companion object {
    private const val DEFAULT_REFERENCE = "HEAD"
  }

  override fun invoke(entity: WorkflowEntity) {
    jobScheduler.scheduleRecurrently(entity.options["cronExpression"] as String) { notify(entity) }
  }

  /**
   * This method should be public, so jobrunr can cache argument and method body for further
   * invocations
   */
  fun notify(entity: WorkflowEntity) {
    entity.branches.forEach { branch ->
      notifyWorkflowTriggered(entity.projectKey, branch, DEFAULT_REFERENCE)
    }
  }
}
