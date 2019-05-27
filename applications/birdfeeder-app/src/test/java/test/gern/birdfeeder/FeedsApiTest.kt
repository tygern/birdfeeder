package test.gern.birdfeeder

import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.github.tomakehurst.wiremock.WireMockServer
import com.github.tomakehurst.wiremock.client.WireMock.*
import com.github.tomakehurst.wiremock.matching.EqualToPattern
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
    val restTemplate = testRestTemplate("http://localhost:8090")
    val mapper = jacksonObjectMapper()
    val instagramServer = WireMockServer(8099)

    before {
        instagramServer.stubFor(get(urlPathEqualTo("/cookingforbirds"))
            .withQueryParam("__a", EqualToPattern("1"))
            .willReturn(aResponse()
                .withBodyFile("cookingforbirds.json")))
        instagramServer.start()

        context = SpringApplication(BirdfeederApp::class.java).run {
            setDefaultProperties(Properties().apply {
                setProperty("server.port", "8090")
                setProperty("instagram.url", instagramServer.baseUrl())
            })
            run()
        }
        JdbcTemplate(testDataSource()).execute("delete from feed")
    }

    after {
        SpringApplication.exit(context)
        instagramServer.stop()
    }

    test {
        val postResponse = restTemplate.postForEntity("/feeds", mapOf("name" to "cookingforbirds"), String::class.java)

        assertThat(postResponse.statusCode).isEqualTo(HttpStatus.CREATED)

        val postBody = mapper.readTree(postResponse.body)
        assertThat(postBody["name"].asText()).isEqualTo("cookingforbirds")
        assertThat(postBody["link"].asText()).isEqualTo("http://localhost:8099/cookingforbirds")
        assertThat(postBody["imageUrl"].asText()).isEqualTo("http://example.com/hq_photo")
        assertThat(postBody["description"].asText()).isEqualTo("Here is my biography")

        val getResponse = restTemplate.getForEntity("/feeds", String::class.java)
        assertThat(getResponse.statusCode).isEqualTo(HttpStatus.OK)

        val getBody = mapper.readTree(getResponse.body)[0]

        assertThat(getBody["name"].asText()).contains("cookingforbirds")
        assertThat(getBody["link"].asText()).isEqualTo("http://localhost:8099/cookingforbirds")
        assertThat(getBody["imageUrl"].asText()).isEqualTo("http://example.com/hq_photo")
        assertThat(getBody["description"].asText()).isEqualTo("Here is my biography")

    }

    test("duplicates") {
        val postResponse = restTemplate.postForEntity("http://localhost:8090/feeds", mapOf(
            "name" to "cookingforbirds",
            "link" to "http://localhost:8099/cookingforbirds",
            "imageUrl" to "http://example.com/hq_photo",
            "description" to "Here is my biography"), String::class.java)

        assertThat(postResponse.statusCode).isEqualTo(HttpStatus.CREATED)

        val duplicateResponse = restTemplate.postForEntity("http://localhost:8090/feeds", mapOf(
            "name" to "cookingforbirds",
            "link" to "http://localhost:8099/cookingforbirds",
            "imageUrl" to "http://example.com/hq_photo",
            "description" to "Here is my biography"), String::class.java)

        assertThat(duplicateResponse.statusCode).isEqualTo(HttpStatus.UNPROCESSABLE_ENTITY)
        assertThat(duplicateResponse.body).isEqualTo("Feed named cookingforbirds already exists")
    }
})
