package md.gvart.buildtracker.gateway.validation.strategy

import md.gvart.buildtracker.domain.entity.TriggerType
import md.gvart.buildtracker.domain.entity.WorkflowRegistration
import md.gvart.buildtracker.gateway.validation.error.InvalidFieldValueException
import net.datafaker.Faker
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows

class OnCommitWorkflowTypeValidatorTest {

  val onCommitWorkflowTypeValidator = OnCommitWorkflowTypeValidator()

  val faker = Faker()

  @Test
  fun `Should return ON_COMMIT type Test`() {
    // When
    val result = onCommitWorkflowTypeValidator.getType()

    // Then
    assertThat(result).isEqualTo(TriggerType.ON_COMMIT)
  }

  @Test
  fun `Validation succeed Test`() {
    // Given
    val registration =
        WorkflowRegistration(
            projectKey = faker.name().name(),
            repositoryUrl = faker.domain().validDomain("company-name"),
            branches = listOf("master"),
            pipelineFileName = faker.name().name(),
            triggerType = TriggerType.ON_COMMIT)
    // When
    onCommitWorkflowTypeValidator.validate(registration)

    // Then no errors were thrown
  }
  @Test
  fun `Should fail validation due to missing repositoryUrl  Test`() {
    // Given
    val registration =
        WorkflowRegistration(
            projectKey = faker.name().name(),
            repositoryUrl = "",
            branches = listOf("master"),
            pipelineFileName = faker.name().name(),
            triggerType = TriggerType.ON_COMMIT)

    // Then exception should be thrown
    assertThrows<InvalidFieldValueException> {
      // When
      onCommitWorkflowTypeValidator.validate(registration)
    }
  }

  @Test
  fun `Should fail validation due to missing projectKey Test`() {
    // Given
    val registration =
        WorkflowRegistration(
            projectKey = "",
            repositoryUrl = faker.domain().validDomain("company-name"),
            branches = listOf("master"),
            pipelineFileName = faker.name().name(),
            triggerType = TriggerType.ON_COMMIT)

    // Then exception should be thrown
    assertThrows<InvalidFieldValueException> {
      // When
      onCommitWorkflowTypeValidator.validate(registration)
    }
  }

  @Test
  fun `Should fail validation due to missing branches Test`() {
    // Given
    val registration =
        WorkflowRegistration(
            projectKey = faker.name().name(),
            repositoryUrl = faker.domain().validDomain("company-name"),
            branches = emptyList(),
            pipelineFileName = faker.name().name(),
            triggerType = TriggerType.ON_COMMIT)

    // Then exception should be thrown
    assertThrows<InvalidFieldValueException> {
      // When
      onCommitWorkflowTypeValidator.validate(registration)
    }
  }

  @Test
  fun `Should fail validation due to missing pipelineFileName  Test`() {
    // Given
    val registration =
        WorkflowRegistration(
            projectKey = faker.name().name(),
            repositoryUrl = faker.domain().validDomain("company-name"),
            branches = listOf("master"),
            pipelineFileName = "",
            triggerType = TriggerType.ON_COMMIT)

    // Then exception should be thrown
    assertThrows<InvalidFieldValueException> {
      // When
      onCommitWorkflowTypeValidator.validate(registration)
    }
  }
}
