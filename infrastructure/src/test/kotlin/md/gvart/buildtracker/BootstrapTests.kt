package md.gvart.buildtracker

import md.gvart.buildtracker.config.MongoTestContainersConfig
import org.junit.jupiter.api.Test
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.ContextConfiguration

@SpringBootTest
@ContextConfiguration(classes = [MongoTestContainersConfig::class])
class BootstrapTests {

  @Test fun contextLoads() {}
}
