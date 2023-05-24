package md.gvart.buildtracker.domain.gateway

import md.gvart.buildtracker.domain.entity.WorkflowEntity

interface FindWorkflows : (String) -> Set<WorkflowEntity>
