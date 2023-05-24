package md.gvart.buildtracker.gateway.queue

import io.awspring.cloud.sqs.operations.SqsTemplate
import md.gvart.buildtracker.config.properties.DestinationProperties
import md.gvart.buildtracker.domain.entity.WorkflowTriggerNotification
import md.gvart.buildtracker.domain.gateway.SendWorkflowTriggerNotification
import org.apache.logging.log4j.kotlin.Logging
import org.springframework.stereotype.Component

@Component
class DefaultSendWorkflowTriggerNotification(
    private val sqsTemplate: SqsTemplate,
    private val destinationProperties: DestinationProperties
) : SendWorkflowTriggerNotification, Logging {
  override fun invoke(notification: WorkflowTriggerNotification) {
    logger.info("Sending trigger notification: $notification")

    sqsTemplate.send<WorkflowTriggerNotification> {
      it.apply {
        queue(destinationProperties.queueName)
        payload(notification)
        messageGroupId(notification.repositoryUrl)
      }
    }
  }
}
