package md.gvart.buildtracker.config

import org.springframework.boot.devtools.restart.RestartScope
import org.springframework.boot.test.context.TestConfiguration
import org.springframework.context.annotation.Bean
import org.testcontainers.containers.localstack.LocalStackContainer
import org.testcontainers.utility.DockerImageName
import software.amazon.awssdk.services.sqs.SqsAsyncClient
import software.amazon.awssdk.services.sqs.model.QueueAttributeName

@TestConfiguration
class SqsTestContainersConfig {

  @Bean
  @RestartScope
  fun localStackContainer(sqsClient: SqsAsyncClient): LocalStackContainer {
    val container =
        LocalStackContainer(DockerImageName.parse("localstack/localstack:2.0"))
            .withServices(LocalStackContainer.Service.SQS)
            .withEnv("AWS_DEFAULT_REGION", "us-east-1")
            .withEnv("HOSTNAME_EXTERNAL", "localhost")

    sqsClient
        .createQueue {
          it.queueName("main.fifo")
              .attributes(
                  mapOf(
                      QueueAttributeName.FIFO_QUEUE to "true",
                      QueueAttributeName.FIFO_THROUGHPUT_LIMIT to "perMessageGroupId",
                      QueueAttributeName.DEDUPLICATION_SCOPE to "messageGroup",
                  ))
        }
        .get()

    container.execInContainer(
        "awslocal", "sqs", "create-queue", "--queue-name", "main.fifo", "--region", "us-east-1")
    return container
  }
}
