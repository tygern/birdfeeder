package test.gern.birdfeeder

import io.damo.aspen.Test
import org.assertj.core.api.Assertions.assertThat
import org.gern.birdfeeder.BirdfeederApp
import org.springframework.boot.SpringApplication
import org.springframework.context.ConfigurableApplicationContext
import org.springframework.web.client.RestTemplate
import java.util.*

class BirdfeederAppTest : Test({

    var context : ConfigurableApplicationContext? = null

    before {
        context = SpringApplication(BirdfeederApp::class.java).run {
            setDefaultProperties(Properties().apply { setProperty("server.port", "8090")})
            run()
        }

    }

    after {
        SpringApplication.exit(context)
    }

    test {
        val result = RestTemplate().getForObject("http://localhost:8090", String::class.java)

        assertThat(result).isEqualTo("birdfeeder")
    }
})
