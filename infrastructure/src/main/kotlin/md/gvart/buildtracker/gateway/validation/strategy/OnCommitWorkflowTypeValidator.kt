package md.gvart.buildtracker.gateway.validation.strategy

import md.gvart.buildtracker.domain.entity.TriggerType
import org.springframework.stereotype.Component

@Component
class OnCommitWorkflowTypeValidator : AbstractWorkflowTypeValidator() {
  override fun getType() = TriggerType.ON_COMMIT
}
