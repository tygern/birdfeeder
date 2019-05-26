package org.gern.birdfeeder.feeds

import org.springframework.dao.DuplicateKeyException
import org.springframework.jdbc.core.JdbcTemplate
import javax.sql.DataSource

class FeedRepository(datasource: DataSource) {
    private val jdbc = JdbcTemplate(datasource)

    fun create(name: String): Result<FeedRecord> = try {
        jdbc.update("insert into feed (name) values (?)", name)
        Result.Success(FeedRecord(name))
    } catch (e: DuplicateKeyException) {
        Result.Failure("Feed named $name already exists")
    }

    fun list(): List<FeedRecord> =
        jdbc.query("select * from feed") { rs, _ ->
            FeedRecord(rs.getString("name"))
        }
}

data class FeedRecord(val name: String)
