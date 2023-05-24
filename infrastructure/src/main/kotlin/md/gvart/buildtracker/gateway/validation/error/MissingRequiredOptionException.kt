package md.gvart.buildtracker.gateway.validation.error

import md.gvart.buildtracker.domain.entity.TriggerType
import md.gvart.buildtracker.domain.exception.DomainException

class MissingRequiredOptionException(triggerType: TriggerType, parameterName: String) :
    DomainException("Missing parameter $.options.$parameterName for trigger type: $triggerType")
