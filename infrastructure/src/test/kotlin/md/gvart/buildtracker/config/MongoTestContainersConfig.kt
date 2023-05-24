package md.gvart.buildtracker.config

import org.springframework.boot.devtools.restart.RestartScope
import org.springframework.boot.test.context.TestConfiguration
import org.springframework.boot.testcontainers.service.connection.ServiceConnection
import org.springframework.context.annotation.Bean
import org.testcontainers.containers.MongoDBContainer
import org.testcontainers.utility.DockerImageName

@TestConfiguration
class MongoTestContainersConfig {

  @Bean
  @ServiceConnection
  @RestartScope
  fun mongoDBContainer(): MongoDBContainer {
    return MongoDBContainer(DockerImageName.parse("mongo:4.2"))
        .withEnv("MONGO_INITDB_DATABASE", "workflows")
  }
}
