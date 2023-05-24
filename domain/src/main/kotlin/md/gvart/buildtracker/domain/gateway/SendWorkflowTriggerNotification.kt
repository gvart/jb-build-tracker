package md.gvart.buildtracker.domain.gateway

import md.gvart.buildtracker.domain.entity.WorkflowTriggerNotification

interface SendWorkflowTriggerNotification : (WorkflowTriggerNotification) -> Unit
