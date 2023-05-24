package md.gvart.buildtracker.domain

import java.time.Instant
import md.gvart.buildtracker.domain.entity.WorkflowTriggerNotification
import md.gvart.buildtracker.domain.exception.ProjectWorkflowNotFound
import md.gvart.buildtracker.domain.gateway.FindWorkflows
import md.gvart.buildtracker.domain.gateway.SendWorkflowTriggerNotification

class NotifyWorkflowTriggered(
    private val findWorkflows: FindWorkflows,
    private val sendWorkflowTriggerNotification: SendWorkflowTriggerNotification
) : (String, BranchName, CommitHash) -> Unit {
  override fun invoke(projectKey: String, branch: BranchName, commitHash: CommitHash) {
    val workflows = findWorkflows(projectKey)

    if (workflows.isEmpty()) {
      throw ProjectWorkflowNotFound(projectKey)
    }

    return workflows
        .filter { branchMatches(branch, it.branches) }
        .map {
          WorkflowTriggerNotification(
              it.repositoryUrl,
              branch,
              commitHash,
              it.pipelineFileName,
              Instant.now(),
          )
        }
        .forEach { sendWorkflowTriggerNotification(it) }
  }

  private fun branchMatches(branch: String, branches: List<String>): Boolean {
    return branches.any { it.toRegex().matches(branch) }
  }
}

typealias BranchName = String

typealias CommitHash = String
