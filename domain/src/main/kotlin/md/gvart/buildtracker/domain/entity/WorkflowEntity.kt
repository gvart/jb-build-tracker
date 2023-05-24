package md.gvart.buildtracker.domain.entity

data class WorkflowEntity(
    val id: String? = null,
    val projectKey: String,
    val repositoryUrl: String,
    val branches: List<String>,
    val pipelineFileName: String,
    val triggerType: TriggerType,
    val options: Map<String, Any>,
)
