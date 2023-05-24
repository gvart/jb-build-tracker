package md.gvart.buildtracker.entrypoint.web

import md.gvart.buildtracker.domain.NotifyWorkflowTriggered
import md.gvart.buildtracker.domain.RegisterWorkflowTrigger
import md.gvart.buildtracker.domain.entity.WorkflowEntity
import md.gvart.buildtracker.domain.entity.WorkflowRegistration
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.ResponseStatus
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/v1/workflows")
class WorkflowTriggerEntrypoint(
    private val registerWorkflowTrigger: RegisterWorkflowTrigger,
    private val notifyWorkflowTriggered: NotifyWorkflowTriggered,
) {

  @PostMapping
  fun registerWorkflow(
      @RequestBody request: WorkflowRegistration,
  ): ResponseEntity<WorkflowEntity> {
    val entity = registerWorkflowTrigger.invoke(request)

    return ResponseEntity.status(HttpStatus.CREATED).body(entity)
  }

  @ResponseStatus(HttpStatus.ACCEPTED)
  @GetMapping("/webhook")
  fun triggerWorkflowOnCommit(
      @RequestParam("projectKey") projectKey: String,
      @RequestParam("branch") branch: String,
      @RequestParam("commitHash") commitHash: String,
  ) {
    notifyWorkflowTriggered(projectKey, branch, commitHash)
  }
}
