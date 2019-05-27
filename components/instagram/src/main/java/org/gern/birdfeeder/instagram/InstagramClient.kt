package org.gern.birdfeeder.instagram

import com.fasterxml.jackson.databind.ObjectMapper
import org.gern.birdfeeder.result.Result
import org.springframework.web.client.HttpClientErrorException
import org.springframework.web.client.RestClientException
import org.springframework.web.client.RestTemplate
import org.springframework.web.client.getForEntity
import java.net.URI

class InstagramClient(
    private val instagramUri: URI,
    private val restTemplate: RestTemplate,
    private val objectMapper: ObjectMapper
) {
    fun profileInfo(name: String): Result<InstagramProfile> {
        val profileUri = instagramUri.resolve(URI.create("/$name?__a=1"))

        return try {
            val entity = restTemplate.getForEntity<String>(profileUri)
            val userJson = objectMapper.readTree(entity.body)["graphql"]["user"]

            Result.Success(InstagramProfile(
                name = name,
                link = instagramUri.resolve(URI.create("/$name")),
                imageUrl = userJson["profile_pic_url_hd"].asText(),
                description = userJson["biography"].asText()
            ))
        } catch (e: HttpClientErrorException) {
            Result.Failure("Instagram profile not found")
        } catch (e: RestClientException) {
            Result.Failure("Error contacting Instagram")
        }
    }
}

data class InstagramProfile(
    val name: String,
    val link: URI,
    val imageUrl: String,
    val description: String
)
