package md.gvart.buildtracker.entrypoint.web

import io.awspring.cloud.sqs.operations.SqsTemplate
import java.time.Duration
import md.gvart.buildtracker.config.MongoTestContainersConfig
import md.gvart.buildtracker.config.SqsTestContainersConfig
import md.gvart.buildtracker.domain.entity.TriggerType
import md.gvart.buildtracker.domain.entity.WorkflowEntity
import md.gvart.buildtracker.domain.entity.WorkflowTriggerNotification
import md.gvart.buildtracker.gateway.database.repository.WorkflowEntityRepository
import net.datafaker.Faker
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.http.MediaType
import org.springframework.test.context.ContextConfiguration
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.get
import org.springframework.test.web.servlet.post
import org.testcontainers.shaded.org.awaitility.Awaitility.await

@SpringBootTest
@AutoConfigureMockMvc
@ContextConfiguration(classes = [MongoTestContainersConfig::class, SqsTestContainersConfig::class])
class WorkflowTriggerEntrypointTest {

  @Autowired lateinit var testClient: MockMvc

  @Autowired lateinit var repository: WorkflowEntityRepository

  @Autowired lateinit var sqsTemplate: SqsTemplate

  val faker = Faker()

  @BeforeEach
  fun cleanup() {
    repository.deleteAll()
  }

  @Test
  fun `Should register a workflow of type ON_COMMIT Test`() {
    // Given
    val projectKey = faker.book().genre()
    val repositoryUrl = faker.domain().validDomain("company")
    val branches =
        listOf("master", "dev").joinToString(prefix = "[", postfix = "]", transform = { "\"$it\"" })
    val pipelineFileName = faker.book().genre()
    val triggerType = TriggerType.ON_COMMIT

    // When
    val result =
        testClient.post("/api/v1/workflows") {
          contentType = MediaType.APPLICATION_JSON
          content =
              """
                {
                "projectKey": "$projectKey",
                "repositoryUrl": "$repositoryUrl",
                "branches":  $branches,
                "pipelineFileName": "$pipelineFileName",
                "triggerType": "$triggerType"
                }
            """
        }

    // Then workflow was saved and created entity was returned
    result.andExpect {
      status { isCreated() }

      jsonPath("$.id") { isNotEmpty() }
      jsonPath("$.projectKey") { value(projectKey) }
      jsonPath("$.repositoryUrl") { value(repositoryUrl) }
      jsonPath("$.branches") { isArray() }
      jsonPath("$.pipelineFileName") { value(pipelineFileName) }
      jsonPath("$.triggerType") { value(triggerType.name) }
      jsonPath("$.options") { isEmpty() }
    }
  }

  @Test
  fun `Should register a workflow of type SHEDULED Test`() {
    // Given
    val projectKey = faker.book().genre()
    val repositoryUrl = faker.domain().validDomain("company")
    val branches =
        listOf("master", "dev").joinToString(prefix = "[", postfix = "]", transform = { "\"$it\"" })
    val pipelineFileName = faker.book().genre()
    val triggerType = TriggerType.SCHEDULED
    val cronExpression = "*/30 * * * * *"

    // When
    val result =
        testClient.post("/api/v1/workflows") {
          contentType = MediaType.APPLICATION_JSON
          content =
              """
                {
                "projectKey": "$projectKey",
                "repositoryUrl": "$repositoryUrl",
                "branches":  $branches,
                "pipelineFileName": "$pipelineFileName",
                "triggerType": "$triggerType",
                "options": {
                  "cronExpression": "$cronExpression"
                 }
                }
            """
        }

    // Then workflow was saved and created entity was returned
    result.andExpect {
      status { isCreated() }

      jsonPath("$.id") { isNotEmpty() }
      jsonPath("$.projectKey") { value(projectKey) }
      jsonPath("$.repositoryUrl") { value(repositoryUrl) }
      jsonPath("$.branches") { isArray() }
      jsonPath("$.pipelineFileName") { value(pipelineFileName) }
      jsonPath("$.triggerType") { value(triggerType.name) }
      jsonPath("$.options.cronExpression") { value(cronExpression) }
    }
  }

  @Test
  fun `Should return 400 if cronExpression is not passed for a workflow of type SHEDULED Test`() {
    // Given
    val projectKey = faker.book().genre()
    val repositoryUrl = faker.domain().validDomain("company")
    val branches =
        listOf("master", "dev").joinToString(prefix = "[", postfix = "]", transform = { "\"$it\"" })
    val pipelineFileName = faker.book().genre()
    val triggerType = TriggerType.SCHEDULED

    // When
    val result =
        testClient.post("/api/v1/workflows") {
          contentType = MediaType.APPLICATION_JSON
          content =
              """
                {
                "projectKey": "$projectKey",
                "repositoryUrl": "$repositoryUrl",
                "branches":  $branches,
                "pipelineFileName": "$pipelineFileName",
                "triggerType": "$triggerType"
                }
            """
        }

    // Then a bad requests was returned
    result.andExpect { status { isBadRequest() } }
  }

  @Test
  fun `Should return 400 if projectKey is empty Test`() {
    // Given
    val projectKey = ""
    val repositoryUrl = faker.domain().validDomain("company")
    val branches =
        listOf("master", "dev").joinToString(prefix = "[", postfix = "]", transform = { "\"$it\"" })
    val pipelineFileName = faker.book().genre()
    testBadRequestScenario(projectKey, repositoryUrl, branches, pipelineFileName)
  }

  @Test
  fun `Should return 400 if repositoryUrl is empty Test`() {
    // Given
    val projectKey = faker.book().genre()
    val repositoryUrl = ""
    val branches =
        listOf("master", "dev").joinToString(prefix = "[", postfix = "]", transform = { "\"$it\"" })
    val pipelineFileName = faker.book().genre()
    testBadRequestScenario(projectKey, repositoryUrl, branches, pipelineFileName)
  }

  @Test
  fun `Should return 400 if branches are empty Test`() {
    // Given
    val projectKey = faker.book().genre()
    val repositoryUrl = faker.domain().validDomain("company")
    val branches = "[]"
    val pipelineFileName = faker.book().genre()
    testBadRequestScenario(projectKey, repositoryUrl, branches, pipelineFileName)
  }

  @Test
  fun `Should return 400 if pipelineFileName is empty Test`() {
    // Given
    val projectKey = faker.book().genre()
    val repositoryUrl = faker.domain().validDomain("company")
    val branches =
        listOf("master", "dev").joinToString(prefix = "[", postfix = "]", transform = { "\"$it\"" })
    val pipelineFileName = ""

    testBadRequestScenario(projectKey, repositoryUrl, branches, pipelineFileName)
  }

  private fun testBadRequestScenario(
      projectKey: String?,
      repositoryUrl: String?,
      branches: String,
      pipelineFileName: String?
  ) {
    val triggerType = TriggerType.ON_COMMIT

    // When
    val result =
        testClient.post("/api/v1/workflows") {
          contentType = MediaType.APPLICATION_JSON
          content =
              """
                    {
                    "projectKey": "$projectKey",
                    "repositoryUrl": "$repositoryUrl",
                    "branches":  $branches,
                    "pipelineFileName": "$pipelineFileName",
                    "triggerType": "$triggerType"
                    }
                """
        }

    // Then a bad requests was returned
    result.andExpect { status { isBadRequest() } }
  }

  @Test
  fun `Should accept the webhook for an existent request Test`() {
    // Given
    val projectKey = faker.name().firstName()
    val repositoryUrl = faker.domain().validDomain("company")
    val branches = listOf("master", "dev")
    val pipelineFileName = faker.book().genre()
    val triggerType = TriggerType.ON_COMMIT
    val entity =
        WorkflowEntity(
            null, projectKey, repositoryUrl, branches, pipelineFileName, triggerType, emptyMap())
    repository.save(entity)

    val expectedBranch = "master"
    val expectedCommitHash = "hash"

    // When
    val result =
        testClient.get("/api/v1/workflows/webhook") {
          param("projectKey", projectKey)
          param("branch", expectedBranch)
          param("commitHash", expectedCommitHash)
        }

    // Then api return 202, message is sent to SQS and can be consumed
    result.andExpect { status { isAccepted() } }

    await().atMost(Duration.ofSeconds(5)).untilAsserted {
      val sqsMessage = sqsTemplate.receive("main.fifo", WorkflowTriggerNotification::class.java)
      sqsMessage.ifPresent {
        val payload = it.payload
        assertThat(payload.repositoryUrl).isEqualTo(repositoryUrl)
        assertThat(payload.branch).isEqualTo(expectedBranch)
        assertThat(payload.commitHash).isEqualTo(expectedCommitHash)
        assertThat(payload.pipelineFileName).isEqualTo(pipelineFileName)
      }
    }
  }

  @Test
  fun `Should return 404 if no workflows were found for give projectKey Test`() {
    // Given, provided projectKey doesn't exists

    // When
    val result =
        testClient.get("/api/v1/workflows/webhook") {
          param("projectKey", "unknown")
          param("branch", "expectedBranch")
          param("commitHash", "commitHash")
        }

    // Then api return 404
    result.andExpect { status { isNotFound() } }
  }
}
