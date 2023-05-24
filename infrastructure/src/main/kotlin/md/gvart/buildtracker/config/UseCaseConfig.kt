package md.gvart.buildtracker.config

import md.gvart.buildtracker.domain.NotifyWorkflowTriggered
import md.gvart.buildtracker.domain.RegisterWorkflowTrigger
import md.gvart.buildtracker.domain.gateway.FindWorkflows
import md.gvart.buildtracker.domain.gateway.PersistWorkflow
import md.gvart.buildtracker.domain.gateway.ScheduleWorkflow
import md.gvart.buildtracker.domain.gateway.SendWorkflowTriggerNotification
import md.gvart.buildtracker.domain.gateway.ValidateWorkflowRegistration
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class UseCaseConfig {

  @Bean
  fun registerWorkflowTrigger(
      validateWorkflowRegistration: ValidateWorkflowRegistration,
      persistWorkflow: PersistWorkflow,
      scheduleWorkflow: ScheduleWorkflow
  ) = RegisterWorkflowTrigger(validateWorkflowRegistration, persistWorkflow, scheduleWorkflow)

  @Bean
  fun notifyWorkflowTriggered(
      findWorkflows: FindWorkflows,
      sendWorkflowTriggerNotification: SendWorkflowTriggerNotification
  ) = NotifyWorkflowTriggered(findWorkflows, sendWorkflowTriggerNotification)
}
