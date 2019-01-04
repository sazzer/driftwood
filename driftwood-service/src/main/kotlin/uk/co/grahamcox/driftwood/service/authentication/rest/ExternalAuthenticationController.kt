package uk.co.grahamcox.driftwood.service.authentication.rest

import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestMethod
import org.springframework.web.bind.annotation.RestController
import uk.co.grahamcox.driftwood.service.authentication.AuthenticatorRegistry

/**
 * Controller for managing External Authentication requests
 */
@RestController
@RequestMapping("/api/authentication/external")
class ExternalAuthenticationController(private val authenticatorRegistry: AuthenticatorRegistry) {
    /**
     * List the services that can be used
     * @return the list of service names
     */
    @RequestMapping(method = [RequestMethod.GET])
    fun listServices() = authenticatorRegistry.names
}
