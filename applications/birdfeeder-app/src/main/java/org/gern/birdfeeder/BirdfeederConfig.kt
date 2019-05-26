package org.gern.birdfeeder

import org.gern.birdfeeder.feeds.FeedRepository
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import javax.sql.DataSource

@Configuration
class BirdfeederConfig {

    @Bean
    fun feedRepository(dataSource: DataSource) = FeedRepository(dataSource)
}
