package md.gvart.buildtracker.domain.exception

class ProjectWorkflowNotFound(projectKey: String) :
    DomainException("Workflows for project=$projectKey were not found")
