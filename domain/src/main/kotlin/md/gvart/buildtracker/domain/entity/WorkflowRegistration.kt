package md.gvart.buildtracker.domain.entity

data class WorkflowRegistration(
    val projectKey: String,
    val repositoryUrl: String,
    val branches: List<String>,
    val pipelineFileName: String,
    val triggerType: TriggerType,
    val options: Map<String, Any> = DEFAULT_OPTIONS,
) {
  companion object {
    private val DEFAULT_OPTIONS = emptyMap<String, Any>()
  }
}
