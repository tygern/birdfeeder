package test.gern.birdfeeder.instagram

import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import io.damo.aspen.Test
import org.assertj.core.api.Assertions.assertThat
import org.gern.birdfeeder.instagram.InstagramClient
import org.gern.birdfeeder.result.Result
import org.springframework.http.HttpMethod
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.test.web.client.MockRestServiceServer
import org.springframework.test.web.client.match.MockRestRequestMatchers.method
import org.springframework.test.web.client.match.MockRestRequestMatchers.requestTo
import org.springframework.test.web.client.response.MockRestResponseCreators.withStatus
import org.springframework.test.web.client.response.MockRestResponseCreators.withSuccess
import org.springframework.web.client.RestTemplate
import java.net.URI


class InstagramClientTest : Test({
    val restTemplate = RestTemplate()
    val testServer = MockRestServiceServer.bindTo(restTemplate).build()

    val client = InstagramClient(
        instagramUri = URI("http://instagram.example.com"),
        restTemplate = restTemplate,
        objectMapper = jacksonObjectMapper()
    )

    before {
        testServer.reset()
    }

    test("profile info") {
        testServer
            .expect(requestTo("http://instagram.example.com/finnsadventures?__a=1"))
            .andExpect(method(HttpMethod.GET))
            .andRespond(
                withSuccess(javaClass.getResource("/finnsadventures.json").readText(), MediaType.APPLICATION_JSON)
            )

        val result = client.profileInfo("finnsadventures")

        require(result is Result.Success)

        assertThat(result.value.name).isEqualTo("finnsadventures")
        assertThat(result.value.link).isEqualTo(URI.create("http://instagram.example.com/finnsadventures"))
        assertThat(result.value.imageUrl).isEqualTo("http://example.com/hq_photo")
        assertThat(result.value.description).isEqualTo("Here is my biography")

        testServer.verify()
    }

    test("profile info note found") {
        testServer
            .expect(requestTo("http://instagram.example.com/finnsadventures?__a=1"))
            .andRespond(withStatus(HttpStatus.NOT_FOUND))

        val result = client.profileInfo("finnsadventures")

        require(result is Result.Failure)

        assertThat(result.reason).isEqualTo("Instagram profile not found")
    }

    test("profile info failure") {
        testServer
            .expect(requestTo("http://instagram.example.com/finnsadventures?__a=1"))
            .andRespond(withStatus(HttpStatus.INTERNAL_SERVER_ERROR))

        val result = client.profileInfo("finnsadventures")

        require(result is Result.Failure)

        assertThat(result.reason).isEqualTo("Error contacting Instagram")
    }
})
