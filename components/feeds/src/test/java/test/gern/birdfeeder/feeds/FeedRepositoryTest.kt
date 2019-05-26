package test.gern.birdfeeder.feeds

import io.damo.aspen.Test
import org.assertj.core.api.Assertions.assertThat
import org.gern.birdfeeder.feeds.FeedRecord
import org.gern.birdfeeder.feeds.FeedRepository
import org.gern.birdfeeder.feeds.Result
import org.gern.birdfeeder.testsupport.testDataSource
import org.springframework.jdbc.core.JdbcTemplate

class FeedRepositoryTest : Test({
    val ds = testDataSource()
    val jdbc = JdbcTemplate(ds)

    val repo = FeedRepository(ds)

    before {
        jdbc.execute("delete from feed")
    }

    test("create") {
        val result = repo.create("fred")

        assertThat(result).isEqualTo(Result.Success(FeedRecord(name = "fred")))
        assertThat(repo.list()[0]).isEqualTo(FeedRecord(name = "fred"))
    }

    test("create multiple") {
        assertThat(repo.create("fred")).isInstanceOfAny(Result.Success::class.java)

        val secondCreateResult = repo.create("fred")
        assertThat(secondCreateResult).isInstanceOfAny(Result.Failure::class.java)
        require(secondCreateResult is Result.Failure)

        assertThat(secondCreateResult.reason).isEqualTo("Feed named fred already exists")
    }

    test("list") {
        jdbc.execute("insert into feed (name) values ('pete')")

        val record = repo.list()[0]

        assertThat(record.name).isEqualTo("pete")
    }
})
