package md.gvart.buildtracker.gateway.database

import md.gvart.buildtracker.domain.entity.WorkflowEntity
import md.gvart.buildtracker.domain.entity.WorkflowRegistration
import md.gvart.buildtracker.domain.gateway.PersistWorkflow
import md.gvart.buildtracker.gateway.database.repository.WorkflowEntityRepository
import org.apache.logging.log4j.kotlin.Logging
import org.springframework.stereotype.Component

@Component
class DefaultPersistWorkflow(private val repository: WorkflowEntityRepository) :
    PersistWorkflow, Logging {
  override fun invoke(registration: WorkflowRegistration): WorkflowEntity {
    logger.debug("Saving workflow: $registration")

    val intermediateEntity = mapToEntity(registration)
    return repository.save(intermediateEntity)
  }

  private fun mapToEntity(registration: WorkflowRegistration) =
      WorkflowEntity(
          projectKey = registration.projectKey,
          repositoryUrl = registration.repositoryUrl,
          branches = registration.branches,
          pipelineFileName = registration.pipelineFileName,
          triggerType = registration.triggerType,
          options = registration.options,
      )
}
