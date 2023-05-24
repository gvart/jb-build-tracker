package md.gvart.buildtracker.gateway.database

import md.gvart.buildtracker.config.MongoTestContainersConfig
import md.gvart.buildtracker.domain.entity.TriggerType
import md.gvart.buildtracker.domain.entity.WorkflowRegistration
import md.gvart.buildtracker.gateway.database.repository.WorkflowEntityRepository
import net.datafaker.Faker
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.ContextConfiguration

@SpringBootTest
@ContextConfiguration(classes = [MongoTestContainersConfig::class])
class DefaultPersistWorkflowTest {

  @Autowired lateinit var defaultPersistWorkflow: DefaultPersistWorkflow

  @Autowired lateinit var repository: WorkflowEntityRepository

  val faker = Faker()

  @BeforeEach
  fun cleanup() {
    repository.deleteAll()
  }

  @Test
  fun `Should persist registration Test`() {
    // Given
    val projectKey = faker.book().genre()
    val repositoryUrl = faker.domain().validDomain("company")
    val branches = listOf("master", "dev")
    val pipelineFileName = faker.book().genre()
    val triggerType = TriggerType.SCHEDULED
    val entity =
        WorkflowRegistration(
            projectKey, repositoryUrl, branches, pipelineFileName, triggerType, emptyMap())
    // When
    val result = defaultPersistWorkflow.invoke(entity)

    // Then
    assertThat(result.id).isNotEmpty
    assertThat(result.projectKey).isEqualTo(projectKey)
    assertThat(result.repositoryUrl).isEqualTo(repositoryUrl)
    assertThat(result.branches).containsAll(branches)
    assertThat(result.pipelineFileName).isEqualTo(pipelineFileName)
    assertThat(result.triggerType).isEqualTo(triggerType)
  }
}
