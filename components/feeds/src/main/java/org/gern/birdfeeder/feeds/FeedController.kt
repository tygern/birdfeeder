package org.gern.birdfeeder.feeds

import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
class FeedController(
    private val repo : FeedRepository
) {
    @PostMapping("/feeds")
    fun create(@RequestBody feed: FeedEntity): ResponseEntity<FeedEntity> {
        val record = repo.create(feed.name)
        return ResponseEntity(feedEntity(record), HttpStatus.CREATED)
    }

    @GetMapping("/feeds")
    fun list() = repo.list().map(this::feedEntity)

    private fun feedEntity(record: FeedRecord) = FeedEntity(record.name)
}

data class FeedEntity(val name: String)
