package test.gern.birdfeeder.feeds

import io.damo.aspen.Test
import org.assertj.core.api.Assertions.assertThat
import org.gern.birdfeeder.feeds.FeedRecord
import org.gern.birdfeeder.feeds.JdbcFeedRepository
import org.gern.birdfeeder.result.Result
import org.gern.birdfeeder.testsupport.testDataSource
import org.springframework.jdbc.core.JdbcTemplate

class FeedRepositoryTest : Test({
    val ds = testDataSource()
    val jdbc = JdbcTemplate(ds)

    val repo = JdbcFeedRepository(ds)

    before {
        jdbc.execute("delete from feed")
    }

    test("create") {
        val expectedRecord = FeedRecord(
            name = "fred",
            link = "http://example.com",
            imageUrl = "http://example.com/image",
            description = "pretty cool \uD83D\uDDFA"
        )

        val result = repo.create(
            name = "fred",
            link = "http://example.com",
            imageUrl = "http://example.com/image",
            description = "pretty cool \uD83D\uDDFA"
        )

        assertThat(result).isEqualTo(Result.Success(expectedRecord))
        assertThat(repo.list()[0]).isEqualTo(expectedRecord)
    }

    test("create multiple") {
        assertThat(repo.create(
            name = "fred",
            link = "http://example.com",
            imageUrl = "http://example.com/image",
            description = "pretty cool"
        )).isInstanceOfAny(Result.Success::class.java)

        val secondCreateResult = repo.create("fred", "", "", "")
        require(secondCreateResult is Result.Failure)

        assertThat(secondCreateResult.reason).isEqualTo("Feed named fred already exists")
    }

    test("list") {
        jdbc.execute("insert into feed (name, link, image_url, description) values" +
            "('pete', 'http://example.com', 'http://example.com/image', 'pretty cool')")

        val record = repo.list()[0]

        assertThat(record).isEqualTo(FeedRecord(
            name = "pete",
            link = "http://example.com",
            imageUrl = "http://example.com/image",
            description = "pretty cool"
        ))
    }
})
