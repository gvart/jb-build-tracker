package md.gvart.buildtracker.domain.gateway

import md.gvart.buildtracker.domain.entity.WorkflowEntity

interface ScheduleWorkflow : (WorkflowEntity) -> Unit
