package uk.co.grahamcox.driftwood.service.openid.rest

import org.slf4j.LoggerFactory
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.*
import uk.co.grahamcox.driftwood.service.clients.ClientData
import uk.co.grahamcox.driftwood.service.clients.ClientId
import uk.co.grahamcox.driftwood.service.clients.ClientNotFoundException
import uk.co.grahamcox.driftwood.service.clients.ClientRetriever
import uk.co.grahamcox.driftwood.service.model.Resource
import uk.co.grahamcox.driftwood.service.openid.scopes.Scope
import uk.co.grahamcox.driftwood.service.openid.scopes.ScopeRegistry
import uk.co.grahamcox.driftwood.service.openid.scopes.UnknownScopesException

/**
 * Controller for handling the calls to the OAuth2 Token endpoint
 */
@RestController
@RequestMapping("/api/oauth2/token")
class TokenController(
        private val clientRetriever: ClientRetriever,
        private val scopeRegistry: ScopeRegistry
) {
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

        if (clientCredentials == null) {
            LOG.warn("No client details provided")
            throw InvalidClientException()
        }

        val client = try {
            clientCredentials.let { clientRetriever.getById(it.clientId) }
        } catch (e: ClientNotFoundException) {
            LOG.warn("Client ID not found", e)
            throw InvalidClientException()
        }

        val scopes = params["scopes"]
                ?.let { scopeRegistry.parseScopes(it) }
                ?: emptySet()

        when(grantType) {
            "client_credentials" -> handleClientCredentials(client, scopes)
            "authorization_code" -> TODO("Authorization Code Grant")
            "refresh_token" -> TODO("Refresh Token Grant")
            null -> throw MissingGrantTypeException()
            else -> throw UnknownGrantTypeException(grantType)
        }
    }

    /**
     * Handle the Client Credentials Grant request
     * @param client The client details
     * @param scopes The scope
     */
    fun handleClientCredentials(client: Resource<ClientId, ClientData>,
                                scopes: Set<Scope>) {
        TODO("Client Credentials Grant")
    }

    /**
     * Handle when an OpenID Exception occurs
     * @param e The error to handle
     */
    @ExceptionHandler(OpenIDException::class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    fun handleOpenIdException(e: OpenIDException) = e.error

    /**
     * Handle when an OpenID Invalid Client Exception occurs
     * @param e The error to handle
     */
    @ExceptionHandler(InvalidClientException::class)
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    fun handleInvalidClientException(e: InvalidClientException) = e.error

    /**
     * Handle when an Unknown Scopes Exception occurs
     * @param e The error to handle
     */
    @ExceptionHandler(UnknownScopesException::class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    fun handleUnknownScopesException(e: UnknownScopesException) = ErrorResponse(
            error = ErrorCode.INVALID_SCOPE,
            description = "The requested scopes were not all valid",
            details = mapOf("scopes" to e.scopes)
    )
}
