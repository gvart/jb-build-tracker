package md.gvart.buildtracker.domain

import md.gvart.buildtracker.domain.entity.TriggerType
import md.gvart.buildtracker.domain.entity.WorkflowEntity
import md.gvart.buildtracker.domain.exception.ProjectWorkflowNotFound
import md.gvart.buildtracker.domain.gateway.FindWorkflows
import md.gvart.buildtracker.domain.gateway.SendWorkflowTriggerNotification
import net.datafaker.Faker
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.junit.jupiter.MockitoExtension
import org.mockito.kotlin.any
import org.mockito.kotlin.argThat
import org.mockito.kotlin.never
import org.mockito.kotlin.times
import org.mockito.kotlin.verify
import org.mockito.kotlin.whenever

@ExtendWith(MockitoExtension::class)
class NotifyWorkflowTriggeredTest {

  @InjectMocks lateinit var notifyWorkflowTriggered: NotifyWorkflowTriggered

  @Mock lateinit var findWorkflows: FindWorkflows

  @Mock lateinit var sendWorkflowTriggerNotification: SendWorkflowTriggerNotification

  val faker = Faker()

  @Test
  fun `Should throw an exception if no workflows were found Test`() {
    // Given
    val projectKey = faker.name().name()
    val branchName = faker.name().name()
    val commitHash = faker.name().name()

    // When
    whenever(findWorkflows.invoke(projectKey)).thenReturn(emptySet())

    assertThrows<ProjectWorkflowNotFound> {
      notifyWorkflowTriggered.invoke(projectKey, branchName, commitHash)
    }

    // Then
    verify(findWorkflows).invoke(projectKey)
    verify(sendWorkflowTriggerNotification, never()).invoke(any())
  }

  @Test
  fun `Should send notification if branch name matches exactly and ignore others Test`() {
    // Given
    val projectKey = faker.name().name()
    val branchName = "master"
    val commitHash = faker.name().name()

    // When
    whenever(findWorkflows.invoke(projectKey))
        .thenReturn(generateWorkflowEntityFor("master", "^dev-.*"))

    notifyWorkflowTriggered.invoke(projectKey, branchName, commitHash)

    // Then
    verify(findWorkflows).invoke(projectKey)
    verify(sendWorkflowTriggerNotification, times(1))
        .invoke(
            argThat {
              assertThat(branch).isEqualTo(branchName)
              assertThat(commitHash).isEqualTo(commitHash)
              true
            })
  }

  @Test
  fun `Should send notification if branch name matches the pattern and ignore others Test`() {
    // Given
    val projectKey = faker.name().name()
    val branchName = "dev-100"
    val commitHash = faker.name().name()

    // When
    whenever(findWorkflows.invoke(projectKey))
        .thenReturn(generateWorkflowEntityFor("master", "^dev-.*"))

    notifyWorkflowTriggered.invoke(projectKey, branchName, commitHash)

    // Then
    verify(findWorkflows).invoke(projectKey)
    verify(sendWorkflowTriggerNotification, times(1))
        .invoke(
            argThat {
              assertThat(branch).isEqualTo(branchName)
              assertThat(commitHash).isEqualTo(commitHash)
              true
            })
  }

  @Test
  fun `Should not send any notification if none branch matches Test`() {
    // Given
    val projectKey = faker.name().name()
    val branchName = "ABC"
    val commitHash = faker.name().name()

    // When
    whenever(findWorkflows.invoke(projectKey))
        .thenReturn(generateWorkflowEntityFor("master", "^dev-.*"))

    notifyWorkflowTriggered.invoke(projectKey, branchName, commitHash)

    // Then
    verify(findWorkflows).invoke(projectKey)
    verify(sendWorkflowTriggerNotification, never()).invoke(any())
  }

  private fun generateWorkflowEntityFor(vararg branchPatters: String): Set<WorkflowEntity> {
    return branchPatters
        .map { WorkflowEntity("", "", "", listOf(it), "", TriggerType.ON_COMMIT, emptyMap()) }
        .toSet()
  }
}
