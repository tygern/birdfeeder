package org.gern.birdfeeder.feeds

import org.gern.birdfeeder.rss.Rss
import org.springframework.http.MediaType
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RestController


@RestController
class RssFeedController {
    @GetMapping("/feeds/{name}/rss", produces = [MediaType.APPLICATION_XML_VALUE])
    fun find(@PathVariable name: String) = Rss(Rss.Channel(
        title = "Frank Knarf",
        link = "http://fillmurray.com",
        description = "Frank Knarf's twitter feed: $name",
        image = Rss.Channel.Image(
            title = "Frank Knarf",
            link = "http://fillmurray.com",
            url = "http://fillmurray.com/200/200"
        ),
        item = listOf(
            Rss.Channel.Item(
                title = "Here's a post title",
                link = "http://fillmurray.com",
                description = "<img src=\"http://fillmurray.com/300/301\">",
                author = "@frank",
                guid = "http://fillmurray.com/300/301"
            ),
            Rss.Channel.Item(
                title = "Here's another post title",
                link = "http://fillmurray.com",
                description = "<img src=\"http://fillmurray.com/300/302\">",
                author = "@frank",
                guid = "http://fillmurray.com/300/302"
            )
        )
    ))
}
