package uk.co.grahamcox.driftwood.service.authentication.rest

import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import org.springframework.web.servlet.ModelAndView
import uk.co.grahamcox.driftwood.service.authentication.AuthenticatorRegistry
import uk.co.grahamcox.driftwood.service.clients.ClientId
import uk.co.grahamcox.driftwood.service.clients.ClientRetriever
import uk.co.grahamcox.driftwood.service.openid.token.AccessTokenCreator
import uk.co.grahamcox.driftwood.service.openid.token.AccessTokenSerializer
import javax.servlet.http.Cookie
import javax.servlet.http.HttpServletResponse

/**
 * Controller for managing External Authentication requests
 */
@RestController
@RequestMapping("/api/authentication/external")
class ExternalAuthenticationController(
        private val authenticatorRegistry: AuthenticatorRegistry,
        private val defaultClientId: ClientId,
        private val clientRetriever: ClientRetriever,
        private val accessTokenCreator: AccessTokenCreator,
        private val accessTokenSerializer: AccessTokenSerializer) {
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

    /**
     * Complete authentication with the given service
     * @param service The service to use
     * @param expectedService The service that we expected the callback from
     * @param expectedState The state that we expect in the callback
     * @param params The parameters from the callback
     * @return The model and view for the authentication callback
     */
    @RequestMapping(value = ["/{service}/callback"], method = [RequestMethod.GET])
    fun completeAuthentication(@PathVariable("service") service: String,
                               @CookieValue("externalAuthenticationService") expectedService: String?,
                               @CookieValue("externalAuthenticationState") expectedState: String?,
                               @RequestParam params: Map<String, String>): ModelAndView {
        val user = authenticatorRegistry[service].authenticateUser(params, expectedState)

        val openidClient = clientRetriever.getById(defaultClientId)
        val accessToken = accessTokenCreator.createToken(openidClient, user.identity.id, emptySet())
        val serialized = accessTokenSerializer.serializeAccessToken(accessToken)

        return ModelAndView("authentication/success", mapOf(
                "user" to user,
                "accessToken" to accessToken,
                "serialized" to serialized
        ))
    }
}
