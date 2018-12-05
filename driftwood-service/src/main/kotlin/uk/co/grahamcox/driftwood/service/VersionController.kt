package uk.co.grahamcox.driftwood.service

import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestMethod
import org.springframework.web.bind.annotation.RestController

/**
 * Controller for getting the API Version number
 */
@RestController
@RequestMapping("/api/version")
class VersionController {
    /**
     * The API Version number
     */
    @get:RequestMapping(method = [RequestMethod.GET])
    val version = mapOf("version" to "1.0.0")
}
