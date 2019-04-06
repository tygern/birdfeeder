package org.gern.birdfeeder.feeds

data class Feed(
    val title: String,
    val link: String,
    val description: String,
    val image: String,
    val items: List<FeedItem>
)

data class FeedItem(
    val title: String,
    val link: String,
    val description: String,
    val author: String,
    val guid: String
)
