package md.gvart.buildtracker.domain

import kotlin.random.Random
import md.gvart.buildtracker.domain.entity.TriggerType
import md.gvart.buildtracker.domain.entity.WorkflowEntity
import md.gvart.buildtracker.domain.entity.WorkflowRegistration
import md.gvart.buildtracker.domain.gateway.PersistWorkflow
import md.gvart.buildtracker.domain.gateway.ScheduleWorkflow
import md.gvart.buildtracker.domain.gateway.ValidateWorkflowRegistration
import net.datafaker.Faker
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.Mockito.never
import org.mockito.Mockito.verify
import org.mockito.junit.jupiter.MockitoExtension
import org.mockito.kotlin.anyOrNull
import org.mockito.kotlin.whenever

@ExtendWith(MockitoExtension::class)
class RegisterWorkflowTriggerTest {

  @InjectMocks lateinit var registerWorkflowTrigger: RegisterWorkflowTrigger

  @Mock lateinit var validateWorkflowRegistration: ValidateWorkflowRegistration

  @Mock lateinit var persistWorkflow: PersistWorkflow

  @Mock lateinit var scheduleWorkflow: ScheduleWorkflow

  val faker = Faker()

  @Test
  fun `Should not persist the workflow if validation failed Test`() {
    // Given entity and validation fails
    val registration =
        WorkflowRegistration(
            projectKey = faker.name().name(),
            repositoryUrl = faker.domain().validDomain("company-name"),
            branches = generateBranchesList(),
            pipelineFileName = faker.name().name(),
            triggerType = TriggerType.values().random(),
        )

    whenever(validateWorkflowRegistration.invoke(registration)).thenThrow(RuntimeException())

    // When
    assertThrows<RuntimeException> { registerWorkflowTrigger(registration) }

    // Then persist is not invoked
    verify(validateWorkflowRegistration).invoke(registration)
    verify(persistWorkflow, never()).invoke(registration)
  }

  @Test
  fun `Should persist the workflow ON_COMMIT without default values if validation passed Test`() {
    // Given entity and validation fails
    val registration =
        WorkflowRegistration(
            projectKey = faker.name().name(),
            repositoryUrl = faker.domain().validDomain("company-name"),
            branches = generateBranchesList(),
            pipelineFileName = faker.name().name(),
            triggerType = TriggerType.ON_COMMIT)
    mockPersistWorkflowResponse()

    // When
    registerWorkflowTrigger(registration)

    // Then persist is invoked and default options are empty
    verify(validateWorkflowRegistration).invoke(registration)
    verify(persistWorkflow).invoke(registration)
    assertThat(registration.options).isEmpty()
  }

  @Test
  fun `Should persist the workflow SCHEDULED if validation passed and schedule it Test`() {
    // Given entity and validation fails
    val registration =
        WorkflowRegistration(
            projectKey = faker.name().name(),
            repositoryUrl = faker.domain().validDomain("company-name"),
            branches = generateBranchesList(),
            pipelineFileName = faker.name().name(),
            triggerType = TriggerType.SCHEDULED)

    val expectedWorkflowEntity =
        WorkflowEntity(
            id = faker.name().name(),
            projectKey = registration.projectKey,
            repositoryUrl = registration.repositoryUrl,
            branches = registration.branches,
            pipelineFileName = registration.pipelineFileName,
            triggerType = registration.triggerType,
            options = registration.options)
    whenever(persistWorkflow.invoke(registration)).thenReturn(expectedWorkflowEntity)

    // When
    registerWorkflowTrigger(registration)

    // Then persist is invoked and default options are empty
    verify(validateWorkflowRegistration).invoke(registration)
    verify(persistWorkflow).invoke(registration)
    assertThat(registration.options).isEmpty()

    verify(scheduleWorkflow).invoke(expectedWorkflowEntity)
  }

  @Test
  fun `Should persist the workflow ON_COMMIT with options if validation passed Test`() {
    // Given entity and validation fails
    val registration =
        WorkflowRegistration(
            projectKey = faker.name().name(),
            repositoryUrl = faker.domain().validDomain("company-name"),
            branches = generateBranchesList(),
            pipelineFileName = faker.name().name(),
            triggerType = TriggerType.ON_COMMIT,
            options =
                mapOf(
                    "key1" to faker.name().name(),
                    "key2" to faker.name().name(),
                ),
        )
    mockPersistWorkflowResponse()

    // When
    registerWorkflowTrigger(registration)

    // Then
    verify(validateWorkflowRegistration).invoke(registration)
    verify(persistWorkflow).invoke(registration)
  }

  private fun mockPersistWorkflowResponse() {
    val dummyWorkflowEntity =
        WorkflowEntity("", "", "", emptyList(), "", TriggerType.ON_COMMIT, emptyMap())

    whenever(persistWorkflow.invoke(anyOrNull())).thenReturn(dummyWorkflowEntity)
  }

  private fun generateBranchesList(): List<String> {
    return (1..Random.nextInt(5)).map { faker.name().name() }
  }
}
