package org.gern.birdfeeder.feeds

import org.springframework.jdbc.core.JdbcTemplate
import javax.sql.DataSource

class FeedRepository(datasource: DataSource) {
    private val jdbc = JdbcTemplate(datasource)

    fun create(name: String): FeedRecord {
        jdbc.update("insert ignore into feed (name) values (?)", name)
        return FeedRecord(name)
    }

    fun list(): List<FeedRecord> =
        jdbc.query("select * from feed") { rs, _ ->
            FeedRecord(rs.getString("name"))
        }
}

data class FeedRecord(
    val name: String
)
