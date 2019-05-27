package org.gern.birdfeeder.testsupport

import org.springframework.http.client.ClientHttpResponse
import org.springframework.web.client.ResponseErrorHandler
import org.springframework.web.client.RestTemplate
import org.springframework.web.util.UriTemplateHandler
import java.net.URI

fun testRestTemplate(rootUrl: String) = RestTemplate().apply {
    uriTemplateHandler = TestUriTemplateHandler(rootUrl = rootUrl, defaultHandler = uriTemplateHandler)
    errorHandler = TestResponseErrorHandler()
}

class TestUriTemplateHandler(
    private val rootUrl: String,
    private val defaultHandler: UriTemplateHandler
) : UriTemplateHandler {
    override fun expand(uriTemplate: String, uriVariables: MutableMap<String, *>): URI =
        this.defaultHandler.expand(apply(uriTemplate), uriVariables)

    override fun expand(uriTemplate: String, vararg uriVariables: Any?): URI =
        this.defaultHandler.expand(apply(uriTemplate), uriVariables)


    private fun apply(uriTemplate: String) = if (uriTemplate.startsWith("/", ignoreCase = true)) {
        rootUrl + uriTemplate
    } else {
        uriTemplate
    }
}

class TestResponseErrorHandler : ResponseErrorHandler {
    override fun hasError(response: ClientHttpResponse) = false

    override fun handleError(response: ClientHttpResponse) {}
}
