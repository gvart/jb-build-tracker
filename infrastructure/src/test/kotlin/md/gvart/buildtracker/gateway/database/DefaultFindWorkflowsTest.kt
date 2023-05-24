package md.gvart.buildtracker.gateway.database

import md.gvart.buildtracker.config.MongoTestContainersConfig
import md.gvart.buildtracker.domain.entity.TriggerType
import md.gvart.buildtracker.domain.entity.WorkflowEntity
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
class DefaultFindWorkflowsTest {

  @Autowired lateinit var defaultFindWorkflows: DefaultFindWorkflows

  @Autowired lateinit var repository: WorkflowEntityRepository

  val faker = Faker()

  @BeforeEach
  fun cleanup() {
    repository.deleteAll()
  }

  @Test
  fun `Should return existing workflows Test`() {
    // Given
    val expectedProjectKey = faker.name().name()
    val entity =
        WorkflowEntity(
            null,
            expectedProjectKey,
            "repositoryUrl",
            emptyList(),
            "pipelineFileName",
            TriggerType.ON_COMMIT,
            emptyMap())

    repository.save(entity)

    // When
    val result = defaultFindWorkflows.invoke(expectedProjectKey)

    // Then
    assertThat(result).hasSize(1)
  }

  @Test
  fun `Should return an empty list if no workflows were found Test`() {
    // Given no entries in db

    // When
    val result = defaultFindWorkflows.invoke("key")

    // Then
    assertThat(result).isEmpty()
  }
}
