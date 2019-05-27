package test.gern.birdfeeder.feeds

import io.damo.aspen.Test
import io.mockk.every
import io.mockk.mockk
import org.assertj.core.api.Assertions.assertThat
import org.gern.birdfeeder.feeds.FeedRecord
import org.gern.birdfeeder.feeds.FeedService
import org.gern.birdfeeder.instagram.InstagramClient
import org.gern.birdfeeder.instagram.InstagramProfile
import org.gern.birdfeeder.result.Result
import java.net.URI

class FeedServiceTest : Test({
    val repo = FakeFeedRepository()
    val instagramClient = mockk<InstagramClient>()

    val service = FeedService(repo, instagramClient)

    before {
        repo.reset()
    }

    test {
        val pete = FeedRecord(
            name = "pete",
            link = "http://example.com/link",
            imageUrl = "http://example.com/image",
            description = "Check it out"
        )

        every { instagramClient.profileInfo("pete") } returns Result.Success(InstagramProfile(
            name = "pete",
            link = URI.create("http://example.com/link"),
            imageUrl = "http://example.com/image",
            description = "Check it out"
        ))

        val result = service.create("pete")

        require(result is Result.Success)

        assertThat(result.value).isEqualTo(pete)
        assertThat(repo.list()[0]).isEqualTo(pete)
    }

    test("instagram failure") {
        every { instagramClient.profileInfo("pete") } returns Result.Failure("Something went wrong")

        val result = service.create("pete")

        require(result is Result.Failure)

        assertThat(result.reason).isEqualTo("Something went wrong")
        assertThat(repo.list()).isEmpty()
    }
})
