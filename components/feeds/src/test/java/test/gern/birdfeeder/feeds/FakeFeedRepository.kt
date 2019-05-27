package test.gern.birdfeeder.feeds

import org.gern.birdfeeder.feeds.FeedRecord
import org.gern.birdfeeder.feeds.FeedRepository
import org.gern.birdfeeder.result.Result

class FakeFeedRepository : FeedRepository {
    private val feeds = mutableMapOf<String, FeedRecord>()

    override fun create(name: String,
                        link: String,
                        imageUrl: String,
                        description: String): Result<FeedRecord> {
        val record = FeedRecord(name = name,
            link = link,
            imageUrl = imageUrl,
            description = description)

        feeds[name] = record

        return Result.Success(record)
    }

    override fun list() = feeds.values.toList()

    fun reset() {
        feeds.clear()
    }
}
