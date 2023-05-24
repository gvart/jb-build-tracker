package md.gvart.buildtracker.domain.gateway

import md.gvart.buildtracker.domain.entity.WorkflowEntity
import md.gvart.buildtracker.domain.entity.WorkflowRegistration

interface PersistWorkflow : (WorkflowRegistration) -> WorkflowEntity
