package test.gern.birdfeeder.feeds

import io.damo.aspen.Test
import org.assertj.core.api.Assertions.assertThat
import org.gern.birdfeeder.feeds.FeedRecord
import org.gern.birdfeeder.feeds.FeedRepository
import org.mariadb.jdbc.MariaDbDataSource
import org.springframework.jdbc.core.JdbcTemplate

class FeedRepositoryTest : Test({
    val ds = MariaDbDataSource("jdbc:mysql://localhost:3306/feed_test?user=birdfeeder")
    val jdbc = JdbcTemplate(ds)

    val repo = FeedRepository(ds)

    before {
        jdbc.execute("delete from feed")
    }

    test("create") {
        val result = repo.create("fred")

        assertThat(result).isEqualTo(FeedRecord(name = "fred"))
        assertThat(repo.list()[0]).isEqualTo(FeedRecord(name = "fred"))
    }

    test("create multiple") {
        repo.create("fred")
        repo.create("fred")
    }

    test("list") {
        jdbc.execute("insert into feed (name) values ('pete')")

        val record = repo.list()[0]

        assertThat(record.name).isEqualTo("pete")
    }
})
