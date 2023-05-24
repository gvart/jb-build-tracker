package md.gvart.buildtracker.gateway.schedule

import md.gvart.buildtracker.domain.NotifyWorkflowTriggered
import md.gvart.buildtracker.domain.entity.TriggerType
import md.gvart.buildtracker.domain.entity.WorkflowEntity
import org.jobrunr.scheduling.JobScheduler
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.junit.jupiter.MockitoExtension
import org.mockito.kotlin.any
import org.mockito.kotlin.eq
import org.mockito.kotlin.verify

@ExtendWith(MockitoExtension::class)
class DefaultScheduleWorkflowTest {

  @InjectMocks lateinit var scheduleWorkflow: DefaultScheduleWorkflow

  @Mock lateinit var jobScheduler: JobScheduler

  @Mock lateinit var notifyWorkflowTriggered: NotifyWorkflowTriggered

  @Test
  fun `Should schedule job using cron expression Test`() {
    // Given
    val expectedCronExpression = "*/30 * * * * *"

    val entity =
        WorkflowEntity(
            "",
            "",
            "",
            emptyList(),
            "",
            TriggerType.ON_COMMIT,
            mapOf("cronExpression" to expectedCronExpression))

    // When
    scheduleWorkflow.invoke(entity)

    // Then
    verify(jobScheduler).scheduleRecurrently(eq(expectedCronExpression), any())
  }

  @Test
  fun `Should send individual notification for each branch Test`() {
    // Given
    val branches = listOf("master", "dev")
    val entity = WorkflowEntity("", "projectKey", "", branches, "", TriggerType.ON_COMMIT, mapOf())

    // When
    scheduleWorkflow.notify(entity)

    // Then
    verify(notifyWorkflowTriggered).invoke(entity.projectKey, branches[0], "HEAD")
    verify(notifyWorkflowTriggered).invoke(entity.projectKey, branches[1], "HEAD")
  }
}
