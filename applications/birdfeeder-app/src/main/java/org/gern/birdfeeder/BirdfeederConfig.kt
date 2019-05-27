package org.gern.birdfeeder

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import org.gern.birdfeeder.feeds.FeedRepository
import org.gern.birdfeeder.feeds.FeedService
import org.gern.birdfeeder.feeds.JdbcFeedRepository
import org.gern.birdfeeder.instagram.InstagramClient
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.web.client.RestTemplate
import java.net.URI
import javax.sql.DataSource

@Configuration
class BirdfeederConfig {

    @Bean
    fun objectMapper() = jacksonObjectMapper()

    @Bean
    fun feedRepository(dataSource: DataSource): FeedRepository = JdbcFeedRepository(dataSource)

    @Bean
    fun feedService(
        feedRepository: FeedRepository,
        instagramClient: InstagramClient
    ) = FeedService(feedRepository, instagramClient)

    @Bean
    fun instagramClient(
        @Value("\${instagram.url}") instagramUri: String,
        restTemplate: RestTemplate,
        objectMapper: ObjectMapper
    ) = InstagramClient(
        instagramUri = URI.create(instagramUri),
        restTemplate = restTemplate,
        objectMapper = objectMapper
    )

    @Bean
    fun restTemplate() = RestTemplate()
}
