package org.gern.birdfeeder.feeds

import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RestController

@RestController
class FeedController(
    private val repo: FeedRepository
) {
    @PostMapping("/feeds", produces = [MediaType.APPLICATION_JSON_VALUE])
    fun create(@RequestBody feed: FeedEntity): ResponseEntity<FeedEntity> {
        val record = repo.create(feed.name)
        return ResponseEntity(feedEntity(record), HttpStatus.CREATED)
    }

    @GetMapping("/feeds", produces = [MediaType.APPLICATION_JSON_VALUE])
    fun list() = repo.list().map(this::feedEntity)

    private fun feedEntity(record: FeedRecord) = FeedEntity(record.name)
}

data class FeedEntity(val name: String)
