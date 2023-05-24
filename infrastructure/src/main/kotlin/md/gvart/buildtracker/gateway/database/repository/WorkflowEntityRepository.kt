package md.gvart.buildtracker.gateway.database.repository

import md.gvart.buildtracker.domain.entity.TriggerType
import md.gvart.buildtracker.domain.entity.WorkflowEntity
import org.springframework.data.mongodb.repository.MongoRepository

interface WorkflowEntityRepository : MongoRepository<WorkflowEntity, String> {
  fun findAllByProjectKeyAndTriggerType(
      projectKey: String,
      triggerType: TriggerType
  ): Set<WorkflowEntity>
}
