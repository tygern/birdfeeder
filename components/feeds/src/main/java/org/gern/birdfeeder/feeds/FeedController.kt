package org.gern.birdfeeder.feeds

import org.gern.birdfeeder.result.Result
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RestController

@RestController
class FeedController(
    private val service: FeedService
) {
    @PostMapping("/feeds", produces = [MediaType.APPLICATION_JSON_VALUE])
    fun create(@RequestBody feed: FeedCreateEntity) =
        when (val result = service.create(feed.name)) {
            is Result.Success -> ResponseEntity(feedEntity(result.value), HttpStatus.CREATED)
            is Result.Failure -> ResponseEntity(result.reason, HttpStatus.UNPROCESSABLE_ENTITY)
        }

    @GetMapping("/feeds", produces = [MediaType.APPLICATION_JSON_VALUE])
    fun list() = service.list().map(this::feedEntity)

    private fun feedEntity(record: FeedRecord) = FeedEntity(
        record.name,
        record.link,
        record.imageUrl,
        record.description
    )
}

data class FeedCreateEntity(val name: String)

data class FeedEntity(
    val name: String,
    val link: String,
    val imageUrl: String,
    val description: String
)
