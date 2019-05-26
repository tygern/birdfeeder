package test.gern.birdfeeder

import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import io.damo.aspen.Test
import org.assertj.core.api.Assertions.assertThat
import org.gern.birdfeeder.BirdfeederApp
import org.gern.birdfeeder.testsupport.testDataSource
import org.gern.birdfeeder.testsupport.testRestTemplate
import org.springframework.boot.SpringApplication
import org.springframework.context.ConfigurableApplicationContext
import org.springframework.http.HttpStatus
import org.springframework.jdbc.core.JdbcTemplate
import java.util.*

class FeedsApiTest : Test({
    var context: ConfigurableApplicationContext? = null
    val restTemplate = testRestTemplate()
    val mapper = jacksonObjectMapper()

    before {
        context = SpringApplication(BirdfeederApp::class.java).run {
            setDefaultProperties(Properties().apply { setProperty("server.port", "8090") })
            run()
        }
        JdbcTemplate(testDataSource()).execute("delete from feed")
    }

    after {
        SpringApplication.exit(context)
    }

    test {
        val postResponse = restTemplate.postForEntity("/feeds", mapOf("name" to "tygern"), String::class.java)

        assertThat(postResponse.statusCode).isEqualTo(HttpStatus.CREATED)

        val postBody = mapper.readTree(postResponse.body)
        assertThat(postBody["name"].asText()).isEqualTo("tygern")

        val getResponse = restTemplate.getForEntity("/feeds", String::class.java)
        assertThat(getResponse.statusCode).isEqualTo(HttpStatus.OK)
        val getBody = mapper.readTree(getResponse.body)

        assertThat(getBody.map { it["name"].asText() }).contains("tygern")
    }

    test("duplicates") {
        val postResponse = restTemplate.postForEntity("http://localhost:8090/feeds", mapOf("name" to "mary"), String::class.java)
        assertThat(postResponse.statusCode).isEqualTo(HttpStatus.CREATED)

        val duplicateResponse = restTemplate.postForEntity("http://localhost:8090/feeds", mapOf("name" to "mary"), String::class.java)
        assertThat(duplicateResponse.statusCode).isEqualTo(HttpStatus.UNPROCESSABLE_ENTITY)
        assertThat(duplicateResponse.body).isEqualTo("Feed named mary already exists")
    }
})
