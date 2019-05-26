package org.gern.birdfeeder.rss

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlElementWrapper
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement

@JacksonXmlRootElement(localName = "rss")
data class Rss(val channel: Channel) {
    @JacksonXmlProperty(isAttribute = true)
    val version = "2.0"

    data class Channel(
        val title: String,
        val link: String,
        val description: String,
        val image: Image,

        @JacksonXmlElementWrapper(useWrapping = false)
        val item: List<Item>
    ) {
        data class Image(
            val title: String,
            val link: String,
            val url: String
        )

        data class Item(
            val title: String,
            val link: String,
            val description: String,
            val author: String,
            val guid: String
        )
    }
}
