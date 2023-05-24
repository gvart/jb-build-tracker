package md.gvart.buildtracker.config.properties

import org.springframework.boot.context.properties.ConfigurationProperties

@ConfigurationProperties(prefix = "application.destination")
data class DestinationProperties(val queueName: String)
