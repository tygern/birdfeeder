package test.gern.birdfeeder

import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import io.damo.aspen.Test
import org.assertj.core.api.Assertions.assertThat
import org.gern.birdfeeder.BirdfeederApp
import org.springframework.boot.SpringApplication
import org.springframework.context.ConfigurableApplicationContext
import org.springframework.http.HttpStatus
import org.springframework.web.client.RestTemplate
import java.util.*

class FeedsApiTest : Test({
    var context: ConfigurableApplicationContext? = null
    val restTemplate = RestTemplate()
    val mapper = jacksonObjectMapper()

    before {
        context = SpringApplication(BirdfeederApp::class.java).run {
            setDefaultProperties(Properties().apply { setProperty("server.port", "8090") })
            run()
        }
    }

    after {
        SpringApplication.exit(context)
    }

    test {
        val postResponse = restTemplate.postForEntity("http://localhost:8090/feeds", mapOf("name" to "tygern"), String::class.java)

        assertThat(postResponse.statusCode).isEqualTo(HttpStatus.CREATED)

        val postBody = mapper.readTree(postResponse.body)
        assertThat(postBody["name"].asText()).isEqualTo("tygern")

        val getResponse = restTemplate.getForEntity("http://localhost:8090/feeds", String::class.java)
        assertThat(getResponse.statusCode).isEqualTo(HttpStatus.OK)
        val getBody = mapper.readTree(getResponse.body)

        assertThat(getBody.map { it["name"].asText() }).contains("tygern")
    }
})
