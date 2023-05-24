package md.gvart.buildtracker

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.context.properties.ConfigurationPropertiesScan
import org.springframework.boot.runApplication

@SpringBootApplication @ConfigurationPropertiesScan class Bootstrap

fun main(args: Array<String>) {
  runApplication<Bootstrap>(*args)
}
