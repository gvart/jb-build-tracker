package md.gvart.buildtracker.gateway.validation.strategy

import md.gvart.buildtracker.domain.entity.TriggerType
import md.gvart.buildtracker.domain.entity.WorkflowRegistration
import md.gvart.buildtracker.gateway.validation.error.InvalidFieldValueException
import md.gvart.buildtracker.gateway.validation.error.MissingRequiredOptionException
import net.datafaker.Faker
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows

class ScheduledWorkflowTypeValidatorTest {

  val validator = ScheduledWorkflowTypeValidator()

  val faker = Faker()

  @Test
  fun `Should return SCHEDULED type Test`() {
    // When
    val result = validator.getType()

    // Then
    assertThat(result).isEqualTo(TriggerType.SCHEDULED)
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
            triggerType = TriggerType.SCHEDULED,
            options = mapOf("cronExpression" to "*/30 * * * * *"))
    // When
    validator.validate(registration)

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
            triggerType = TriggerType.SCHEDULED)

    // Then exception should be thrown
    assertThrows<InvalidFieldValueException> {
      // When
      validator.validate(registration)
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
            triggerType = TriggerType.SCHEDULED)

    // Then exception should be thrown
    assertThrows<InvalidFieldValueException> {
      // When
      validator.validate(registration)
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
            triggerType = TriggerType.SCHEDULED)

    // Then exception should be thrown
    assertThrows<InvalidFieldValueException> {
      // When
      validator.validate(registration)
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
            triggerType = TriggerType.SCHEDULED)

    // Then exception should be thrown
    assertThrows<InvalidFieldValueException> {
      // When
      validator.validate(registration)
    }
  }

  @Test
  fun `Should fail validation due to missing cronExpression Test`() {
    // Given
    val registration =
        WorkflowRegistration(
            projectKey = faker.name().name(),
            repositoryUrl = faker.domain().validDomain("company-name"),
            branches = listOf("master"),
            pipelineFileName = faker.name().name(),
            triggerType = TriggerType.SCHEDULED)

    // Then exception should be thrown
    assertThrows<MissingRequiredOptionException> {
      // When
      validator.validate(registration)
    }
  }
}
