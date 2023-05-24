package md.gvart.buildtracker.gateway.validation

import md.gvart.buildtracker.domain.entity.TriggerType
import md.gvart.buildtracker.domain.entity.WorkflowRegistration
import md.gvart.buildtracker.gateway.validation.strategy.OnCommitWorkflowTypeValidator
import md.gvart.buildtracker.gateway.validation.strategy.ScheduledWorkflowTypeValidator
import net.datafaker.Faker
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.Spy
import org.mockito.junit.jupiter.MockitoExtension
import org.mockito.kotlin.any
import org.mockito.kotlin.doNothing
import org.mockito.kotlin.never
import org.mockito.kotlin.verify

@ExtendWith(MockitoExtension::class)
class DefaultValidateWorkflowRegistrationTest {

  lateinit var defaultValidateWorkflowRegistration: DefaultValidateWorkflowRegistration

  val faker = Faker()

  @Spy
  val onCommitWorkflowTypeValidator: OnCommitWorkflowTypeValidator = OnCommitWorkflowTypeValidator()

  @Spy
  val scheduledWorkflowTypeValidator: ScheduledWorkflowTypeValidator =
      ScheduledWorkflowTypeValidator()

  @BeforeEach
  fun setup() {
    defaultValidateWorkflowRegistration =
        DefaultValidateWorkflowRegistration(
            listOf(onCommitWorkflowTypeValidator, scheduledWorkflowTypeValidator))
  }

  @Test
  fun `Should delegate validation to onCommitWorkflowTypeValidator Test`() {
    // Given
    val registration =
        WorkflowRegistration(
            projectKey = faker.name().name(),
            repositoryUrl = faker.domain().validDomain("company-name"),
            branches = emptyList(),
            pipelineFileName = faker.name().name(),
            triggerType = TriggerType.ON_COMMIT)

    doNothing().`when`(onCommitWorkflowTypeValidator).validate(any())

    // When
    defaultValidateWorkflowRegistration.invoke(registration)

    // Then
    verify(onCommitWorkflowTypeValidator).validate(registration)
    verify(scheduledWorkflowTypeValidator, never()).validate(any())
  }

  @Test
  fun `Should delegate validation to scheduledWorkflowTypeValidator Test`() {
    // Given
    val registration =
        WorkflowRegistration(
            projectKey = faker.name().name(),
            repositoryUrl = faker.domain().validDomain("company-name"),
            branches = emptyList(),
            pipelineFileName = faker.name().name(),
            triggerType = TriggerType.SCHEDULED)

    doNothing().`when`(scheduledWorkflowTypeValidator).validate(any())

    // When
    defaultValidateWorkflowRegistration.invoke(registration)

    // Then
    verify(onCommitWorkflowTypeValidator, never()).validate(registration)
    verify(scheduledWorkflowTypeValidator).validate(any())
  }
}
