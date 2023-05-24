package md.gvart.buildtracker.domain.gateway

import md.gvart.buildtracker.domain.entity.WorkflowRegistration

interface ValidateWorkflowRegistration : (WorkflowRegistration) -> Unit
