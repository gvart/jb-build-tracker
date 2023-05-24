package md.gvart.buildtracker.domain.entity

import java.time.Instant

data class WorkflowTriggerNotification(
    val repositoryUrl: String,
    val branch: String,
    val commitHash: String,
    val pipelineFileName: String,
    val dateTime: Instant
)
