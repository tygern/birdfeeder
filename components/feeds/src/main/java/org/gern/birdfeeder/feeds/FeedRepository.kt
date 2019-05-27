package org.gern.birdfeeder.feeds

import org.gern.birdfeeder.result.Result
import org.springframework.dao.DuplicateKeyException
import org.springframework.jdbc.core.JdbcTemplate
import javax.sql.DataSource

interface FeedRepository {
    fun create(
        name: String,
        link: String,
        imageUrl: String,
        description: String
    ): Result<FeedRecord>

    fun list(): List<FeedRecord>
}

class JdbcFeedRepository(datasource: DataSource) : FeedRepository {
    private val jdbc = JdbcTemplate(datasource)

    override fun create(
        name: String,
        link: String,
        imageUrl: String,
        description: String
    ): Result<FeedRecord> = try {
        jdbc.update("insert into feed (name, link, image_url, description) values (?, ?, ?, ?)", name, link, imageUrl, description)
        Result.Success(FeedRecord(name, link, imageUrl, description))
    } catch (e: DuplicateKeyException) {
        Result.Failure("Feed named $name already exists")
    }

    override fun list(): List<FeedRecord> =
        jdbc.query("select * from feed") { rs, _ ->
            FeedRecord(
                rs.getString("name"),
                rs.getString("link"),
                rs.getString("image_url"),
                rs.getString("description")
            )
        }
}

data class FeedRecord(
    val name: String,
    val link: String,
    val imageUrl: String,
    val description: String
)
