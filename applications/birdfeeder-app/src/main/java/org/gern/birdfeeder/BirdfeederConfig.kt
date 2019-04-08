package org.gern.birdfeeder

import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import org.gern.birdfeeder.feeds.FeedRepository
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import javax.sql.DataSource

@Configuration
class BirdfeederConfig {

    @Bean
    fun objectMapper() = jacksonObjectMapper()

    @Bean
    fun feedRepository(dataSource: DataSource) = FeedRepository(dataSource)
}
