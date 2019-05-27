package org.gern.birdfeeder.feeds

import org.gern.birdfeeder.instagram.InstagramClient
import org.gern.birdfeeder.result.Result

class FeedService(
    private val repo: FeedRepository,
    private val instagramClient: InstagramClient
) {
    fun create(name: String): Result<FeedRecord> =
        when (val result = instagramClient.profileInfo(name)) {
            is Result.Success -> repo.create(
                name = name,
                link = result.value.link.toString(),
                imageUrl = result.value.imageUrl,
                description = result.value.description
            )
            is Result.Failure -> Result.Failure(result.reason)
        }

    fun list() = repo.list()
}
