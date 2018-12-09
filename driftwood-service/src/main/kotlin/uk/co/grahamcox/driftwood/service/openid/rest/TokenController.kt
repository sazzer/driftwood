package uk.co.grahamcox.driftwood.service.openid.rest

import org.slf4j.LoggerFactory
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestMethod
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController

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
            null -> TODO("No Grant Type provided")
            else -> TODO("Unknown Grant Type")
        }
    }
}
