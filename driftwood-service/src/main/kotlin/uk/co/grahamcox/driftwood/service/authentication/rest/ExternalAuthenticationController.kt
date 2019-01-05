package uk.co.grahamcox.driftwood.service.authentication.rest

import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestMethod
import org.springframework.web.bind.annotation.RestController
import uk.co.grahamcox.driftwood.service.authentication.AuthenticatorRegistry
import javax.servlet.http.Cookie
import javax.servlet.http.HttpServletResponse

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

    /**
     * Start authentication with the given service
     * @param service The service to use
     * @return the details to start authentication
     */
    @RequestMapping(value = ["/{service}/start"], method = [RequestMethod.GET])
    fun startAuthentication(@PathVariable("service") service: String, response: HttpServletResponse): ResponseEntity<Void> {
        val result = authenticatorRegistry[service].startAuthentication()

        response.addCookie(Cookie("externalAuthenticationService", service))
        response.addCookie(Cookie("externalAuthenticationState", result.state))

        return ResponseEntity.status(HttpStatus.FOUND)
                .location(result.redirect)
                .build<Void>()
    }
}
