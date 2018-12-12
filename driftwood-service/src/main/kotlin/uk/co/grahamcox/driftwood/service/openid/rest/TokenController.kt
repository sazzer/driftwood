package uk.co.grahamcox.driftwood.service.openid.rest

import org.slf4j.LoggerFactory
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.*

/**
 * Controller for handling the calls to the OAuth2 Token endpoint
 */
@RestController
@RequestMapping("/api/oauth2/token")
class TokenController {
    companion object {
        /** The logger to use */
        private val LOG = LoggerFactory.getLogger(TokenController::class.java)
    }

    /**
     * Handle the request for an Access Token
     * @param grantType The Grant Type to use
     * @param clientCredentials The clients credentials to use
     * @param params The other request parameters
     */
    @RequestMapping(method = [RequestMethod.POST])
    fun tokenRequest(@RequestParam(value = "grant_type", required = false) grantType: String?,
                     clientCredentials: ClientCredentials?,
                     @RequestParam params: Map<String, String>) {
        LOG.info("Processing token request for grant type {}, clients credentials {} and params {}",
                grantType, clientCredentials, params)

        when(grantType) {
            "client_credentials" -> TODO("Client Credentials Grant")
            "authorization_code" -> TODO("Authorization Code Grant")
            "refresh_token" -> TODO("Refresh Token Grant")
            null -> throw MissingGrantTypeException()
            else -> throw UnknownGrantTypeException(grantType)
        }
    }

    /**
     * Handle when an OpenID Exception occurs
     * @param e The error to handle
     */
    @ExceptionHandler(OpenIDException::class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    fun handleOpenIdException(e: OpenIDException) = e.error
}
