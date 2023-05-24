package md.gvart.buildtracker.gateway.database

import md.gvart.buildtracker.domain.entity.TriggerType
import md.gvart.buildtracker.domain.entity.WorkflowEntity
import md.gvart.buildtracker.domain.gateway.FindWorkflows
import md.gvart.buildtracker.gateway.database.repository.WorkflowEntityRepository
import org.apache.logging.log4j.kotlin.Logging
import org.springframework.stereotype.Component

@Component
class DefaultFindWorkflows(private val repository: WorkflowEntityRepository) :
    FindWorkflows, Logging {
  override fun invoke(projectKey: String): Set<WorkflowEntity> {
    logger.debug("Searching all workflows for projectKey=$projectKey")

    val results = repository.findAllByProjectKeyAndTriggerType(projectKey, TriggerType.ON_COMMIT)
    logger.debug("Found ${results.size} workflows for $projectKey")

    return results
  }
}
