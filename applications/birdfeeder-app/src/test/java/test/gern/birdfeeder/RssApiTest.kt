package test.gern.birdfeeder

import com.fasterxml.jackson.dataformat.xml.XmlMapper
import io.damo.aspen.Test
import org.assertj.core.api.Assertions.assertThat
import org.gern.birdfeeder.BirdfeederApp
import org.gern.birdfeeder.testsupport.testRestTemplate
import org.springframework.boot.SpringApplication
import org.springframework.context.ConfigurableApplicationContext
import org.springframework.http.HttpStatus
import java.util.*

class RssApiTest : Test({
    var context: ConfigurableApplicationContext? = null
    val restTemplate = testRestTemplate()

    before {
        context = SpringApplication(BirdfeederApp::class.java).run {
            setDefaultProperties(Properties().apply { setProperty("server.port", "8090") })
            run()
        }
    }

    after {
        SpringApplication.exit(context)
    }

    test("single feed") {
        val getResponse = restTemplate.getForEntity("http://localhost:8090/feeds/frank/rss", String::class.java)
        assertThat(getResponse.statusCode).isEqualTo(HttpStatus.OK)

        val feedBody = XmlMapper().readTree(getResponse.body)["channel"]

        val feedImage = feedBody["image"]
        val firstItem = feedBody["item"]

        assertThat(feedBody["title"].asText()).isEqualTo("Frank Knarf")
        assertThat(feedBody["link"].asText()).isEqualTo("http://fillmurray.com")
        assertThat(feedBody["description"].asText()).isEqualTo("Frank Knarf's twitter feed: frank")

        assertThat(feedImage["title"].asText()).isEqualTo("Frank Knarf")
        assertThat(feedImage["link"].asText()).isEqualTo("http://fillmurray.com")
        assertThat(feedImage["url"].asText()).isEqualTo("http://fillmurray.com/200/200")

        assertThat(firstItem["link"].asText()).isEqualTo("http://fillmurray.com")
        assertThat(firstItem["description"].asText()).isEqualTo("<img src=\"http://fillmurray.com/300/302\">")
        assertThat(firstItem["author"].asText()).isEqualTo("@frank")
        assertThat(firstItem["guid"].asText()).isEqualTo("http://fillmurray.com/300/302")
    }
})
