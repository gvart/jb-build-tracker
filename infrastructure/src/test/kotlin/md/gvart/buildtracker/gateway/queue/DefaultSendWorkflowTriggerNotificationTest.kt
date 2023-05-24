package md.gvart.buildtracker.gateway.queue

import io.awspring.cloud.sqs.operations.SqsTemplate
import java.time.Duration
import java.time.Instant
import md.gvart.buildtracker.config.SqsTestContainersConfig
import md.gvart.buildtracker.domain.entity.WorkflowTriggerNotification
import net.datafaker.Faker
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.ContextConfiguration
import org.testcontainers.shaded.org.awaitility.Awaitility.await

@SpringBootTest
@ContextConfiguration(classes = [SqsTestContainersConfig::class])
class DefaultSendWorkflowTriggerNotificationTest {

  @Autowired lateinit var sendWorkflowTriggerNotification: DefaultSendWorkflowTriggerNotification

  @Autowired lateinit var sqsTemplate: SqsTemplate

  val faker = Faker()

  @Test
  fun `Should send notification to sqs Test`() {
    // Given
    val notification =
        WorkflowTriggerNotification(
            repositoryUrl = faker.domain().fullDomain("company"),
            branch = faker.name().name(),
            commitHash = faker.name().name(),
            pipelineFileName = faker.name().name(),
            dateTime = Instant.now())

    // When
    sendWorkflowTriggerNotification.invoke(notification)

    // Then sqs message was send and can be retrieved from the queue.
    await().atMost(Duration.ofSeconds(5)).untilAsserted {
      val result = sqsTemplate.receive("main.fifo", WorkflowTriggerNotification::class.java)
      result.ifPresent { assertThat(it.payload).isEqualTo(notification) }
    }
  }
}
